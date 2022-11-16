import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import CalSim
import MakeMatchQueue
import Match
import MatchingUser
import Refuse
import Accept
from flask import Flask, request
import json
import pandas as pd
import os
import random

"""
db_url = 'https://matchtest-ab2d8-default-rtdb.firebaseio.com/'
project_ID = 'matchtest-ab2d8'
key_file = 'C:/work/Python/FirebaseMatch_new/FirebaseMatch_new/matchtest-ab2d8-firebase-adminsdk-myh2g-573f6e173e.json'
"""

"""
db_url : Realtime Database url
project_ID = Firbase 프로젝트 ID
key_file : Firestore key file path
"""
db_url = 'https://for-graduate-default-rtdb.firebaseio.com'
project_ID = 'for-graduate'
key_file = 'C:/work/Python/FirebaseMatch_new/FirebaseMatch_new/for-graduate-firebase-adminsdk-no70k-3b97c1ec19.json'

# Firebase 초기화
cred = credentials.Certificate(key_file)
firebase_admin.initialize_app(cred, {
    'databaseURL': db_url,
    'projectId': project_ID
    })


app = Flask(__name__)


# 매칭 큐 생성
@app.route('/match', methods=['POST'])
def post_match_queue():
    if request.method == 'POST':
        nickname = request.json["nickname"]
        message = Match.match(nickname)
    return message


# 매칭 시작 매칭된 상대 uid(fuid) 반환
@app.route('/match/matched-user', methods=['POST'])
def post_matched_user():
    if request.method == 'POST':
        nickname = request.json["nickname"]
        matched_user = MatchingUser.matching_user(nickname)
        result = json.dumps(matched_user)
    return result


# 매칭 수락, 채팅방 생성
@app.route('/match/accept', methods=['POST'])
def post_accept():
    if request.method == 'POST':
        nickname1 = request.json["nickname1"]
        nickname2 = request.json["nickname2"]
        message = Accept.accept(nickname1, nickname2)
    return message


# 매칭 거절
@app.route('/match/refuse', methods=['POST'])
def del_refuse():
    if request.method == 'POST':
        nickname1 = request.json["nickname1"]
        nickname2 = request.json["nickname2"]
        message = Refuse.refuse(nickname1, nickname2)
    return message


if __name__ == "__main__":
    app.run(host='0.0.0.0')


"""
# 곡 추가
db = firestore.client()

path = "C:/work/PlayList"
dir_list = os.listdir(path)

genre_list = ['Classic', 'Jazz', 'Pop', 'Ballad', 'R&B', 'HipHop',
              'CountryMusic', 'Reggae', 'KPOP', 'Trot', 'Dance', 'EDM', 'RockNRoll']


PL = pd.read_csv('C:/work/PlayList/PL00.csv')

song_list = PL['song'].tolist()
singer_list = PL['singer'].tolist()
random_genre_list = random.sample(genre_list, 5)

sifno = {
        'title': song_list,
        'singer': singer_list
    }

uinfo = {
        u'genre': random_genre_list
    }


db.collection('').document(u'sinfo').update(sifno)
db.collection('').document(u'uinfo').update(uinfo)
"""