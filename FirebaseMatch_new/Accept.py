from firebase_admin import firestore
from firebase_admin import db


def accept(nickname1, nickname2):
    fs = firestore.client()

    user_list = fs.collection(u'root').document(u'UserList').get().to_dict()

    uid1 = user_list.get(nickname1)
    uid2 = user_list.get(nickname2)

    queue_path = 'MatchQueue/' + uid1 + '/' + uid2
    del_user = db.reference(queue_path)
    del_user.delete()

    # user2의 finfo에 user1 추가
    finfo1 = {
        uid1: nickname1
    }
    fs.collection(uid2).document(u'finfo').update(finfo1)

    # user1의 finfo에 user2 추가
    finfo2 = {
        uid2: nickname2
    }
    fs.collection(uid1).document(u'finfo').update(finfo2)

    cuid = uid1 + uid2
    cname = nickname1 + '_' + nickname2
    cinfo = {
        cuid: cname
    }
    fs.collection(uid1).document(u'cinfo').update(cinfo)
    fs.collection(uid2).document(u'cinfo').update(cinfo)

    path = cname
    dir = db.reference(path)
    dir.update({'system': 'welcome'})

    return "now " + nickname2 + " is your friends.\n now chat with "
