import boto3
import os
import mimetypes
from PIL import Image, ImageOps

download_bucket = os.environ.get("S3_DOWNLOAD_BUCKET_NAME")

s3_client = boto3.client("s3")


def tag_image_filepath(image_path, tag):
    filename = os.path.basename(image_path)
    folder = os.path.dirname(image_path)
    basename, extension = os.path.splitext(filename)
    tagged_filename = f"{basename}-{tag}{extension}"
    return f"{folder}/{tagged_filename}"


def resize_image(image_path, size):
    with Image.open(image_path) as image:
        image.thumbnail((size, size))
        resized_path = tag_image_filepath(image_path, f"scale{size}")
        image.save(resized_path)
        print(f"Resized {image_path} to {size}px")
    return resized_path


def crop_image(image_path, size, center=(0.5, 0.5)):
    with Image.open(image_path) as image:
        resized_im = ImageOps.fit(image, (size, size), centering=center)
        resized_path = tag_image_filepath(image_path, f"crop{size}")
        resized_im.save(resized_path)
        print(f"Resized {image_path} to {size}px")
    return resized_path


def copy_to_s3(source_path, target_bucket):
    target_key = os.path.basename(source_path)
    content_type, encoding = mimetypes.guess_type(target_key)
    extra_args = {"ContentType": content_type, "ACL": "public-read"}
    s3_client.upload_file(source_path, target_bucket, target_key, ExtraArgs=extra_args)
    print(f"Added {target_key} to {target_bucket}")


def lambda_handler(event, context):
    records = event.get("Records", [])

    for r in records:
        upload_bucket = r["s3"]["bucket"]["name"]
        key = r["s3"]["object"]["key"]

        filename = os.path.basename(key)
        tmp_path = f"/tmp/{filename}"
        print(f"Copying {key} from {upload_bucket} to {tmp_path}")
        s3_client.download_file(upload_bucket, key, tmp_path)

        # Resize to a standard "Large" image
        resize_path = resize_image(tmp_path, 1500)
        copy_to_s3(resize_path, download_bucket)

        # Resize to a cropped square image
        resize_path = crop_image(tmp_path, 600)
        copy_to_s3(resize_path, download_bucket)
