from pyfcm import FCMNotification

class FCMRequest:
    tokenlist = []

    def __init__(self):
        self.push_service = FCMNotification(api_key="AAAAhEC-9UQ:APA91bFp-GLRBB-AWqaaqJwLU1jikQ7P7pCh-6gLYjVRsCsLsH2ihdYEOgWXufu2uax60WGfYud2RcKkpr-4-sPt9FVpvxbA54ALso8PI9rsuED17vk1vQQp_x9EYeGax76Rpr4-8VKV")

        self.messagelist = [
            {"type" : "먹이","body" : "배고파요"},
            {"type": "더움", "body": "더워요"},
            {"type": "추움", "body": "추워요"},
            {"type" : "어두움","body" : "어두워요"},
            {"type" : "탁함","body" : "물 좀 갈아주세요"},
        ]

    def sendStateMessage(self, index):
        result = self.push_service.notify_multiple_devices(registration_ids=FCMRequest.tokenlist, message_title="뻐끔뻐끔", message_body=self.messagelist[index]['body'],  data_message=self.messagelist[index])
        print(result)

    def sendMessageToSingleDevice(self, sentence, token):
        message = {"type" : "대화", "body": sentence}
        result = self.push_service.notify_single_device(registration_id=token, message_title="뻐끔뻐끔", message_body=sentence, data_message=message)
        print(result)

    def setTokenList(self, list):
        FCMRequest.tokenlist = [i[0] for i in list]
        print("savedToken :", FCMRequest.tokenlist)

    def addToken(self, token):
        self.tokenlist.append(token)