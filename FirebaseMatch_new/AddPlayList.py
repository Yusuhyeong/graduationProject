import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import pandas as pd
import os
import random
import numpy as np
import json

# db_url = 'https://dddd-d17f8-default-rtdb.firebaseio.com'
"""
project_ID = 'matchtest-ab2d8'
key_file = 'C:/work/Python/FirebaseMatch_new/FirebaseMatch_new/matchtest-ab2d8-firebase-adminsdk-myh2g-573f6e173e.json'
"""
project_ID = 'for-graduate'
key_file = 'C:/work/Python/FirebaseMatch_new/FirebaseMatch_new/for-graduate-firebase-adminsdk-no70k-3b97c1ec19.json'


cred = credentials.Certificate(key_file)
firebase_admin.initialize_app(cred, {
                'projectId': project_ID
                })
db = firestore.client()

path = "C:/work/PlayList"
dir_list = os.listdir(path)

genre_list = ['Classic', 'Jazz', 'Pop', 'Ballad', 'R&B', 'HipHop',
              'CountryMusic', 'Reggae', 'KPOP', 'Trot', 'Dance', 'EDM', 'RockNRoll']

for playlist in dir_list:
    PL = pd.read_csv(path+'/'+playlist)
    song_list = PL['song'].tolist()
    singer_list = PL['singer'].tolist()
    nickname = playlist.replace(".csv", "")
    random_genre_list = random.sample(genre_list, 5)

    cifno = {
        'fuid': ''
    }

    finfo = {
        'fuid': ''
    }

    sifno = {
        'title': song_list,
        'singer': singer_list
    }

    uinfo = {
        u'age': u'',
        u'nickname': nickname,
        u'pw': u'',
        u'email': u'',
        u'genre': random_genre_list
    }
    db.collection(u'test_PlayList_'+nickname).document(u'cinfo').set(cifno)
    db.collection(u'test_PlayList_'+nickname).document(u'finfo').set(finfo)
    db.collection(u'test_PlayList_'+nickname).document(u'sinfo').set(sifno)
    db.collection(u'test_PlayList_'+nickname).document(u'uinfo').set(uinfo)

    user_info = {
        nickname: 'test_PlayList_'+nickname
    }

    db.collection(u'root').document(u'UserList').update(user_info)
    print(nickname + "_success")
