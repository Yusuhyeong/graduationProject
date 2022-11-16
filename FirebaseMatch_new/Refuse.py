from firebase_admin import firestore
from firebase_admin import db


def refuse(nickname1, nickname2):

    fs = firestore.client()

    user_list = fs.collection(u'root').document(u'UserList').get().to_dict()

    uid1 = user_list.get(nickname1)
    uid2 = user_list.get(nickname2)

    queue_path = 'MatchQueue/' + uid1 + '/' + uid2
    del_user = db.reference(queue_path)
    del_user.delete()

    queue_path = 'MatchQueue/' + uid1 + '/' + 'refuse_list'
    del_user = db.reference(queue_path)
    del_user.update({
                    nickname2: uid2
                    })

    return "You refuse " + nickname2
