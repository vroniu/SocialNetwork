# Quick script to auto populate database (mainly to check the top 10 endpoint)
# Usage (on Windows): python.exe .\auto_populate_db.py 
import http.client
import json

for i in range(100): 
    conn = http.client.HTTPConnection("localhost", 8080)
    payload = json.dumps({
    "author": "Postman",
    "content": "Postman Test",
    "viewCount": i * 10
    })
    headers = {
    'Content-Type': 'application/json'
    }
    conn.request("POST", "/api/v1/posts", payload, headers)
    res = conn.getresponse()
    data = res.read()
    print(data.decode("utf-8"))