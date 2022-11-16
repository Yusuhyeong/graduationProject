import pandas as pd
from numpy import dot
from numpy.linalg import norm


# 자카드 유사도
def jaccard_similarity(X, Y):
    X1 = set(X)
    X2 = set(Y)
    return float(len(X1.intersection(X2)) / len(X1.union(X2)))


# 코사인 유사도
def cos_sim(X, Y):
    return dot(X, Y)/(norm(X)*norm(Y))


# 유사도 계산 및 반환
def score(user1_dic, user2_dic, user1_uinfo, user2_uinfo):

    # sinfo1_singer(list): user1_dic(사용자1의 sinfo)에서 singer만 추출
    sinfo1_singer = user1_dic.get('singer')
    # sinfo1_song(list): user1_dic(사용자1의 sinfo)에서 song만 추출
    sinfo1_song = user1_dic.get('title')
    # uinfo1_genre(list): user1_uinfo에서 genre 리스트 추출
    uinfo1_genre = user1_uinfo.get('genre')

    # playlist1(list): [제목, 가수, 장르]
    playlist1 = [sinfo1_song, sinfo1_singer, uinfo1_genre]
    # user1(dataframe): [제목, 가수, 장르]
    user1 = pd.DataFrame(playlist1).transpose()
    user1.columns = ['song', 'singer', 'genre']

    # user1_subset(list): [(제목, 가수)]
    user1_subset = user1[['song', 'singer']]
    # user1_song(set): (제목, 가수)
    user1_song = [tuple(x) for x in user1_subset.to_numpy()]

    # user1_singer(dataframe): ['singer', 'count'], 사용자1의 가수 count
    user1_singer = user1['singer'].value_counts(ascending=False).rename_axis('singer').reset_index(name='counts')

    # user1_genre(dataframe): ['genre', 'count']
    user1_genre = user1['genre'].value_counts(ascending=False).rename_axis('genre').reset_index(name='count')
    # nan 제거
    user1_genre = user1_genre.dropna()

    # sinfo2_singer(list): user2_dic(사용자2의 sinfo)에서 singer만 추출
    sinfo2_singer = user2_dic.get('singer')
    # sinfo2_song(list): user1_dic(사용자2의 sinfo)에서 song만 추출
    sinfo2_song = user2_dic.get('title')
    # uinfo2_genre(list): user2_uinfo에서 genre 리스트 추출
    uinfo2_genre = user2_uinfo.get('genre')

    # playlist2(list): [제목, 가수, 장르]
    playlist2 = [sinfo2_song, sinfo2_singer, uinfo2_genre]
    # user2(dataframe): [제목, 가수, 장르]
    user2 = pd.DataFrame(playlist2).transpose()
    user2.columns = ['song', 'singer', 'genre']

    # user2_subset(list): [(제목, 가수)]
    user2_subset = user2[['song', 'singer']]
    user2_song = [tuple(x) for x in user2_subset.to_numpy()]

    # user2_singer(dataframe): ['singer', 'count'], 사용자2의 가수 count
    user2_singer = user2['singer'].value_counts(ascending=False).rename_axis('singer').reset_index(name='counts')

    # user2_genre(dataframe): ['genre', 'count']
    user2_genre = user2['genre'].value_counts(ascending=False).rename_axis('genre').reset_index(name='count')
    # nan 제거
    user2_genre = user2_genre.dropna()
    
    # singer_merge(dataframe): ['singer', 'counts_x', 'counts_y]: 사용자1의 가수와 사용자2의 가수를 합침
    singer_merge = pd.merge(user1_singer, user2_singer, on="singer", how='outer')
    # nan 제거
    singer_merge = singer_merge.fillna(0)

    # genre_merge(dataframe): ['genre', 'count_x', 'count_y']: 사용자1의 장르와 사용자2의 장르 합침
    genre_merge = pd.merge(user1_genre, user2_genre, on="genre", how='outer')
    # nan to 0
    genre_merge = genre_merge.fillna(0)

    # 유사도 반환
    # 가수에 대한 코사인 유사도 + 장르에 대한 코사인 유사도 + 곡에 대한 자카드 유사도
    return 0.3 * cos_sim(singer_merge['counts_x'].to_list(), singer_merge['counts_y'].to_list()) \
        + 0.3 * cos_sim(genre_merge['count_x'].to_list(), genre_merge['count_y'].to_list()) \
        + 0.3 * jaccard_similarity(user1_song, user2_song)
