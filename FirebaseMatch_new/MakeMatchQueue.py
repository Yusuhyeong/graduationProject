from firebase_admin import db


def make_match_queue(uid1, uid2):
    path = 'MatchQueue/' + uid1 + '/' + uid2
    ref = db.reference(path)
    ref.update({
                'cname': uid2,
                'fuid': uid2,
                'uid': uid1
                })
