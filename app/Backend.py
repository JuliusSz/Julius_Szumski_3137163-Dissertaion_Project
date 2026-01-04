from fastapi import FastAPI
from pydantic import BaseModel

PROJECT_NAME = "Critter-Collector-MS-3137163"
PROJECT_STORAGE_BUCKET = "critter-collector-ms-3137163.appspot.com"

app = FastAPI()
from fastapi import FastAPI,Request,UploadFile
from fastapi.responses import HTMLResponse,RedirectResponse
from fastapi.staticfiles import StaticFiles
import google.oauth2.id_token
from google.auth.transport import requests
from google.cloud import firestore,storage
from google.cloud.firestore_v1.base_query import FieldFilter

firestore_db = firestore.Client()

firebase_request_adapter = requests.Request()

@app.get("/")


def validateFirebaseToken(id_token):
    if not id_token:
        return None
    
    user_token = None

    try:
        user_token = google.oauth2.id_token.verify_firebase_token(id_token, firebase_request_adapter)
    except ValueError as err:
        print(str(err))
    return user_token

def getUser(user_token):
    user = firestore_db.collection('User').document(user_token['user_id'])

    if not user.get().exists:
        user_data = {
            'email' : user_token['email'],
            'userId':  user_token['user_id'],
            'Username': user_token['email'],
        }
        firestore_db.collection('User').document(user_token['user_id']).set(user_data)
    
    return user

