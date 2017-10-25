from pyfcm import FCMNotification

class FCMRequest:
    """안드로이드측으로 fcm 메시지를 보내기 위한 클래스"""
    tokenlist = []

    def __init__(self):
        self.push_service = FCMNotification(api_key="AAAAhEC-9UQ:APA91bFp-GLRBB-AWqaaqJwLU1jikQ7P7pCh-6gLYjVRsCsLsH2ihdYEOgWXufu2uax60WGfYud2RcKkpr-4-sPt9FVpvxbA54ALso8PI9rsuED17vk1vQQp_x9EYeGax76Rpr4-8VKV")

        self.stateMessageList = [
            {"type" : "먹이","body" : "배고파요"},
            {"type": "더움", "body": "더워요"},
            {"type": "추움", "body": "추워요"},
            {"type" : "어두움","body" : "어두워요"},
            {"type" : "밝음","body" : "너무 밝아요"},
            {"type" : "탁함","body" : "물 좀 갈아주세요"},
        ] #상황에 맞는 상태메시지 정보

    def sendStateMessage(self, index):
        """주어진 상황에 맞는 메시지를 저장된 모든 디바이스에 전송"""
        result = self.push_service.notify_multiple_devices(registration_ids=FCMRequest.tokenlist, message_title="뻐끔뻐끔", message_body=self.stateMessageList[index]['body'], data_message=self.stateMessageList[index])
        print(result)

    def sendTalkingMessage(self, sentence, token):
        """특정 디바이스에 주어진 응답 전송"""
        print(sentence,"sendTalkingMessage-FCMRequest")
        message = {"type" : "대화", "body": sentence}
        result = self.push_service.notify_single_device(registration_id=token, message_title="뻐끔뻐끔", message_body=sentence, data_message=message)
        print(result)

    def sendweathermessage(self, weather, token):
        message = {"type": "날씨", "body": weather}
        result = self.push_service.notify_single_device(registration_id=token, message_title="뻐끔뻐끔",message_body=weather, data_message=message)
        print(result)

    def setTokenList(self, list):
        FCMRequest.tokenlist = [i[0] for i in list]
        print("savedToken :", FCMRequest.tokenlist)

    def addToken(self, token):
        self.tokenlist.append(token)