from pyfcm import FCMNotification

class FCMRequest:
    push_service = FCMNotification(api_key="AAAAhEC-9UQ:APA91bFp-GLRBB-AWqaaqJwLU1jikQ7P7pCh-6gLYjVRsCsLsH2ihdYEOgWXufu2uax60WGfYud2RcKkpr-4-sPt9FVpvxbA54ALso8PI9rsuED17vk1vQQp_x9EYeGax76Rpr4-8VKV")
    token = "eH33GMwkl88:APA91bEZEAJHVVQXNQGOgzf6l9fUTbbiAjn1O-FDWB1FzhzOYbG1lbNfSdCIkB9OaeDi0Rct6w30fDBtaKN5KWIfk9B8PrXsPZrymj-DrgkPg1rFI7QcOQ-WipTdUE9v3banTQc5a6HS"
    message = [
        {"problem" : "먹이","body" : "배고파요"},
        {"problem": "더움", "body": "더워요"},
        {"problem": "추움", "body": "추워요"},
        {"problem" : "어두움","body" : "어두워요"},
        {"problem" : "탁함","body" : "물 좀 갈아주세요"},
    ]

    #result = push_service.notify_single_device(registration_id=token, message_body="뻐끔뻐끔", data_message=data_message)

    def sendStateMessage(index):
        FCMRequest.push_service.notify_single_device(registration_id=FCMRequest.token, message_body="뻐끔뻐끔", data_message=FCMRequest.message[index])
