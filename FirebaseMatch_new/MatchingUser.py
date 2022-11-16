from firebase_admin import firestore
from firebase_admin import db


# 매칭된 사용자의 닉네임 반환
def matching_user(nickname):
    # Firestore 객체 생성
    fs = firestore.client()

    # user_list: 사용자 nickname: uid를 담은 dict
    user_list = fs.collection(u'root').document(u'UserList').get().to_dict()

    # uid: 사용자 nickname을 이용해서 얻은 uid
    uid = user_list.get(nickname)

    queue_path = 'MatchQueue/' + str(uid)
    MatchQueueList = db.reference(queue_path).get()
    MatchQueueKeyList = list(MatchQueueList.keys())

    if MatchQueueKeyList[0] == "refuse_list":
        return MatchQueueList.get(MatchQueueKeyList[1])

    else:
        return MatchQueueList.get(MatchQueueKeyList[0])
