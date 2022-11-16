from firebase_admin import firestore
from firebase_admin import db
import CalSim
import MakeMatchQueue


# 매칭 함수, parameter: 사용자 nickname
def match(nickname):
    # Firestore 객체 생성
    fs = firestore.client()

    # user_list: 사용자 nickname:uid를 담은 dict
    user_list = fs.collection(u'root').document(u'UserList').get().to_dict()

    # uid_list: 사용자 uid를 담은 list
    uid_list = list(user_list.values())

    # uid1: 사용자 nickname을 이용해서 얻은 uid1
    uid1 = user_list.get(str(nickname))
    # uid1_fuid_dict : 사용자1의 fuid dict
    uid1_fuid_dict = fs.collection(str(uid1)).document(u'finfo').get().to_dict()
    # uid1_fuid_dict : 사용자1의 fuid list
    uid1_fuid_list = list(uid1_fuid_dict.keys())

    # uid1의 refuse list
    queue_path = 'MatchQueue/' + str(uid1)
    RefuseListPath = queue_path + '/refuse_list'
    RefuseListDict = db.reference(RefuseListPath).get()
    RefuseList = list(RefuseListDict.values())

    # uid1의 매치큐에 있는 uid list
    MatchQueueListDict = db.reference(queue_path).get()
    MatchQueueList = list(MatchQueueListDict.keys())

    # sinfo1: 사용자(user1, uid1)의 노래정보 dict
    sinfo1 = fs.collection(uid1).document(u'sinfo').get().to_dict()
    uinfo1 = fs.collection(uid1).document(u'uinfo').get().to_dict()

    for uid2 in uid_list:
        """
        아래의 조건을 만족하는 uid2에 대해서만 유사도를 계산
        uid2는 uid1과 다르다.
        uid2는 uid1의 친구가 아니다.
        uid2는 uid1이 거절한 리스트에 존재하지 않는다.
        uid2는 이미 매치큐에 존재하지 않는다.
        """
        if (uid1 != uid2) and \
                (uid2 not in uid1_fuid_list) and \
                (uid2 not in RefuseList) and \
                (uid2 not in MatchQueueList):

            # sinfo2: 사용자(user2, uid2)의 노래정보 dict
            sinfo2 = fs.collection(uid2).document(u'sinfo').get().to_dict()

            if len(sinfo2) == 0:
                continue

            uinfo2 = fs.collection(uid2).document(u'uinfo').get().to_dict()

            # 사용자1과 사용자2의 유사도 계산
            score = CalSim.score(sinfo1, sinfo2, uinfo1, uinfo2)
            t = 0.2
            if score >= t:
                print(uid1, uid2, score)
                MakeMatchQueue.make_match_queue(uid1, uid2)

    return "Make Match Queue"
