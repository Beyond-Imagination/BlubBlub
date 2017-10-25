import sqlite3

class TokenDB:
    """DB 관리 클래스"""
    def __init__(self):
        self.con = sqlite3.connect("token.db")
        self.cur = self.con.cursor()

        try:
            self.cur.execute("create table if not exists FCMtoken(token text primary key);") #DB 생성, 이미 존재시 생성하지 않음
        except sqlite3.Error as e:
            print("Error: ", e.args[0])

    def insertToTokenTable(self, token):
        """주어진 token을 DB에 입력"""
        try:
            self.cur.execute("insert into FCMtoken VALUES (?);", (token,))
            self.con.commit()
            return 1
        except sqlite3.Error as e:
            print("Error: ", e.args[0])
            return 0

    def fetchTokenTable(self):
        """저장된 token list return"""
        try:
            self.cur.execute("select * from FCMtoken;")
            self.con.commit()
        except sqlite3.Error as e:
            print("Error: ", e.args[0])

        return self.cur.fetchall()

class SettingDB():
    """Setting값 관리 클래스"""
    def __init__(self):
        self.con = sqlite3.connect("setting.db")
        self.cur = self.con.cursor()

        try:
            self.cur.execute("create table if not exists Setting("
                             "feeding integer,"
                             "maxtemp integer,"
                             "mintemp integer,"
                             "maxillum integer,"
                             "minillum integer, "
                             "temp_lt integer,"
                             "illum_lt integer,"
                             "turb_lt integer,"
                             "feeding_lt integer);")  # DB 생성, 이미 존재시 생성하지 않음
            tablerow = len(self.fetchSettingTable())
            if tablerow == 0:
                self.cur.execute("insert into Setting VALUES (?,?,?,?,?,?,?,?,?);", (720,30,20,50,20,0,0,0,0,))
        except sqlite3.Error as e:
            print("Error: ", e.args[0])


    def fetchSettingTable(self):
        """저장된 token list return"""
        try:
            self.cur.execute("select * from Setting;")
            self.con.commit()
        except sqlite3.Error as e:
            print("Error: ", e.args[0])

        return self.cur.fetchall()

    def updateSettingTable(self, key, value):
        try:
            self.cur.execute("update Setting set " + str(key) + "=" + str(value) + ";")
            self.con.commit()
            print(self.fetchSettingTable())
        except sqlite3.Error as e:
            print("Error: ", e.args[0])

    def deleteAll(self):
        try:
            self.cur.execute("delete from Setting;")
            self.con.commit()
            print(self.fetchSettingTable())
        except sqlite3.Error as e:
            print("Error: ", e.args[0])