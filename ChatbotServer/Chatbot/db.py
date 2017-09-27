import sqlite3

"""
con = sqlite3.connect("token.db")
cur = con.cursor()

try:
    cur.execute("create table if not exists FCMtoken(token text primary key);")
except sqlite3.Error as e:
    print("Error: ", e.args[0])

def insertToTokenTable(token):
    try:
        cur.execute("insert into FCMtoken VALUES (?);", (token,))
        con.commit()
        return 1
    except sqlite3.Error as e:
        print("Error: ", e.args[0])
        return 0

def fetchTokenTable():
    try:
        cur.execute("select * from FCMtoken;")
        con.commit()
    except sqlite3.Error as e:
        print("Error: ", e.args[0])

    return cur.fetchall()

def updateTokenTable(previous, current):
    try:
        cur.execute("update FCMtoken set token =  ? where token = ?;", (current, previous))
        con.commit()
    except sqlite3.Error as e:
        print("Error: ", e.args[0])

"""
class TokenDB:

    def __init__(self):
        self.con = sqlite3.connect("token.db")
        self.cur = self.con.cursor()

        try:
            self.cur.execute("create table if not exists FCMtoken(token text primary key);")
        except sqlite3.Error as e:
            print("Error: ", e.args[0])

    def insertToTokenTable(self, token):
        try:
            self.cur.execute("insert into FCMtoken VALUES (?);", (token,))
            self.con.commit()
            return 1
        except sqlite3.Error as e:
            print("Error: ", e.args[0])
            return 0

    def fetchTokenTable(self):
        try:
            self.cur.execute("select * from FCMtoken;")
            self.con.commit()
        except sqlite3.Error as e:
            print("Error: ", e.args[0])

        return self.cur.fetchall()