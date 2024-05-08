import json
import os
import uuid
import logging
import boto3
import mimetypes
from botocore.config import Config
from botocore.exceptions import ClientError

logging.basicConfig(
    level=logging.INFO, format="%(asctime)s %(levelname)s (%(lineno)d) :: %(message)s"
)
logger = logging.getLogger(__name__)

conf = Config(region_name="us-east-1")
s3 = boto3.client("s3", config=conf)

upload_bucket = os.environ.get("S3_UPLOAD_BUCKET_NAME")
download_bucket = os.environ.get("S3_DOWNLOAD_BUCKET_NAME")
prefix = "input/"


def create_presigned_url(bucket_name, object_name, content_type, expiration=300):
    logger.info(
        f"Generating signed upload URL for {object_name} to bucket {bucket_name}"
    )
    try:
        response = s3.generate_presigned_url(
            "put_object",
            Params={
                "Bucket": bucket_name,
                "Key": object_name,
                "ContentType": content_type,
            },
            ExpiresIn=expiration,
        )
    except ClientError as e:
        logging.error(e)
        return None

    # The response contains the presigned URL
    return response


def lambda_handler(event, context):
    query_params = event.get("queryStringParameters", None)
    if query_params is None:
        print(json.dumps(event, indent=2))
        raise Exception(f"Missing filename in query string")

    filename = query_params.get("filename", None)

    if filename is None:
        print(json.dumps(query_params, indent=2))
        raise Exception(f"Missing filename in query string")

    filename = filename.lower()
    basename, extension = os.path.splitext(filename)

    if extension not in [".jpg", ".jpeg", ".png", ".gif"]:
        print(json.dumps(event, indent=2))
        raise Exception(f"Unsupported file type '{extension}'")

    content_type, encoding = mimetypes.guess_type(filename)
    if content_type is None:
        raise Exception(f"Invalid mime type {filename}")

    guid = uuid.uuid4()
    object_basename = f"{basename}-{guid}{extension}"
    object_key = f"{prefix}{object_basename}"
    upload_url = create_presigned_url(upload_bucket, object_key, content_type)

    download_url_base, extension = os.path.splitext(
        f"https://{download_bucket}.s3.amazonaws.com/{object_basename}"
    )
    full_url = f"{download_url_base}-scale1500{extension}"
    thumbnail_url = f"{download_url_base}-crop600{extension}"

    responseObj = {
        "status": "OK",
        "upload_url": upload_url,
        "full_url": full_url,
        "thumbnail_url": thumbnail_url,
    }
    responseBody = json.dumps(responseObj, indent=2)

    return {
        "statusCode": 200,
        "headers": {"Content-Type": "application/json"},
        "body": responseBody,
    }
