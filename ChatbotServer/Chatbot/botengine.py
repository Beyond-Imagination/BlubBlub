from bs4 import BeautifulSoup
import urllib.request
from konlpy.tag import Twitter
import os, json, random

class Chatbot:

    def __init__(self):
        self.dict_file = "chatbot-data.json"
        self.dic = {}

        # 딕셔너리가 있다면 읽어 들이기
        if os.path.exists(self.dict_file):
            self.dic = json.load(open(self.dict_file, "r"))

    def register_dic(self, words):
        if len(words) == 0: return
        tmp = ["@"]
        for i in words:
            word = i[0]
            if word == "" or word == "\r\n" or word == "\n": continue
            tmp.append(word)
            if len(tmp) < 3: continue
            if len(tmp) > 3: tmp = tmp[1:]
            self.set_word3(self.dic, tmp)
            if word == "." or word == "?":
                tmp = ["@"]
                continue
        # 딕셔너리가 변경될 때마다 저장하기
        json.dump(self.dic, open(self.dict_file, "w", encoding="utf-8"))

    # 딕셔너리에 글 등록하기
    def set_word3(self, dic, s3):
        print("set_word3")
        w1, w2, w3 = s3
        if not w1 in self.dic: self.dic[w1] = {}
        if not w2 in self.dic[w1]: self.dic[w1][w2] = {}
        if not w3 in self.dic[w1][w2]: self.dic[w1][w2][w3] = 0
        self.dic[w1][w2][w3] += 1

    # 문장 만들기 --- (※2)
    def make_sentence(self, head):
        if not head in self.dic: return ""
        ret = []
        if head != "@": ret.append(head)
        top = self.dic[head]
        w1 = self.word_choice(top)
        w2 = self.word_choice(top[w1])
        ret.append(w1)
        ret.append(w2)
        while True:
            if w1 in self.dic and w2 in self.dic[w1]:
                w3 = self.word_choice(self.dic[w1][w2])
            else:
                w3 = ""
            ret.append(w3)
            if w3 == "." or w3 == "？ " or w3 == "": break
            w1, w2 = w2, w3
        ret = "".join(ret)
        # 띄어쓰기
        params = urllib.parse.urlencode({
            "_callback": "",
            "q": ret
        })
        # 네이버 맞춤법 검사기를 사용합니다.
        data = urllib.request.urlopen("https://m.search.naver.com/p/csearch/dcontent/spellchecker.nhn?" + params)
        data = data.read().decode("utf-8")[1:-2]
        data = json.loads(data)
        data = data["message"]["result"]["html"]
        data = soup = BeautifulSoup(data, "html.parser").getText()
        # 리턴
        return data

    def word_choice(self, sel):
        keys = sel.keys()
        return random.choice(list(keys))

    # 챗봇 응답 만들기 --- (※3)
    def make_reply(self, text):
        # 단어 학습 시키기
        if not text[-1] in [".", "?"]: text += "."
        print(text)
        twitter = Twitter()
        words = twitter.pos(text)
        print(words)
        self.register_dic(words)
        # 사전에 단어가 있다면 그것을 기반으로 문장 만들기
        for word in words:
            face = word[0]
            if face in self.dic: return self.make_sentence(face)
        return self.make_sentence("@")