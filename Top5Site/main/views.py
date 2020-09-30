from django.shortcuts import render, redirect
from django.http import HttpResponse, HttpResponseNotFound, JsonResponse

from django.contrib.staticfiles import finders

from urllib.error import HTTPError

import firebase_admin
from firebase_admin import credentials as adminCredentials, auth as adminAuth, exceptions as firebaseExceptions

import random
import pyrebase

import json
from urllib.parse import unquote

cred = adminCredentials.Certificate(finders.find(
    'key/top-50-9951b-firebase-adminsdk-6n5a9-5a5dfe7f4d.json'))
firebase_admin.initialize_app(cred)

config = {
    "apiKey": "AIzaSyBRRqudJsKGAtpUwkecr1kQu2wFOyRMGZY",
    "authDomain": "top-50-9951b.appspot.com",
    "databaseURL": "https://top-50-9951b.firebaseio.com",
    "storageBucket": "top-50-9951b.appspot.com",
    "serviceAccount": {
        "type": "service_account",
        "project_id": "top-50-9951b",
        "private_key_id": "5a5dfe7f4d8a041537d9a644fd3d148d364d27b0",
        "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYj6QK44SYwhZu\nOwmQnWiBZ7ydDN49rogBr3F5/BaJKmk9q9WhjWciKA0u5j5a5vGLvYN2EmQbnCMe\nxbTfTm5qGpizjXxuKL3/OtmAPJ0jMfA2faRdDMm9aKZsDDtjG5PIiMps8QCMhdzq\nQuYltlxYddnzCkPqf2YP7gPtDNSmv7DbC7gd7x1yDvu5N4+IFtOUF6EhJnDoXp8e\ng6LuKNB4voo8FY1vdetqNPvhk7I6hPlCSWI6M5+y9IRb9FyFnfxwmKk0SNsjqJuF\nuy8WaE5ASZ/nFDcpp68T7yyf/mfo7pxPrrk3uJIuqZlxN4aG0usB3jvmk9LI1uCP\nKXUcILUdAgMBAAECggEABcJWZPrVxJZ/FkYwRD9M9KWf3yXfNeDizX18ASjdCOyK\n8IMOG30lCYsNhFm4vOG++JF85vYmxUwNn1nDTDK/xE2guhZ7PgVXtszX7RlcrTlz\nYfs4OzMCYp4Suj1z+HfQCl/vlFE8vhFHl6hS29WYgtZgFearTazlg3BuwYIcfPbs\nm0jZsfDOpBx+v16Gr7IV5hXZiZ6kkQrXB+sOYTI3wfatneOlKuhT8moPgCWUtL83\nEA7iJ9JCCDutnvRmlg5RTv56HuMrGtVF1uxKFPKfG5OXrNyn6yLilSOk2iz//h3v\n4LjkDKFROlHDMnSQxH9EeA1pO4dmZyAGSSFFtEm+mQKBgQDQOYSkInncHi5DK8ND\nn+jwT182MwVEmb4BGOE/qiK/5xVZC3ERVv8iRo/G6nkv2X2zW2oQS0yPvIxx0DJ8\nIJB7MbYYeRSR7hiirvmWwuSFcrm3Kg2upRo6kbzwASjyi1gFB1k3oF19Zwxicumr\nFRzHzUSDeFcau5KF+qanlUxzFwKBgQC7kJ0e3m7Hi24v+mLoIgvIWNIeaalUExYo\nmgKW+klutqkHU7Gezm1iagIjJVlVpLXA6B6gFpg6izw2snSGHcSENtYIalwFiSHN\nxcXWSK40MNOo8zZ3J8E4519acLLhR3Od1jlH5q8eVk8JcPrzIjz4b80eXF+zcc01\n1IFEkdrJ6wKBgBXJPtiRhuCCA+MhTA/iRlQGafbYxb9Uuq2Qtdica4BapEAp0022\nJYGnklmEpONdxSoj8Wf9COitGKC74Nxd5+AL5nqPCJjwKYGz/wdIIvLXexjv/Hh+\na80e/H68EFW4QKBeEXahf8akJoaScWJmFhnNn1KGH877Oyxrek5kb5hHAoGACPGT\nXGZ019UBMw54auM8tpftpP+a0GR8mQEHAJX8rGfPVYcbIBxtwNSXN3/Pa7MH66Pl\n2fJZ3ejHvT/zKHYA6eEHga04qBbq4rn8fgRHMjvly9eVEEd4AjOeK1zWWsGidLND\nVfddAFBTQnr9rFxElgAWwszaz16sz1VLuK5PxXMCgYEAi7ZFiMJ8IYdjARFa0YKB\nat6NobK+Kza520R+uRFjd+ICVOyOGtsDxxtb0V9nGll11Du82MELgQSwp4pTlOSq\nx2/96a3ay+fDf4b/BJeWdmMWzG28uUktWXorM1itJ4//8UGCW5pymwxPpYxbTeKu\n83jO8l+CqPyvWzfNRyNxLWs=\n-----END PRIVATE KEY-----\n",
        "client_email": "firebase-adminsdk-6n5a9@top-50-9951b.iam.gserviceaccount.com",
        "client_id": "116166562304084917243",
        "auth_uri": "https://accounts.google.com/o/oauth2/auth",
        "token_uri": "https://oauth2.googleapis.com/token",
        "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
        "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-6n5a9%40top-50-9951b.iam.gserviceaccount.com"
    }
}
firebase = pyrebase.initialize_app(config)

auth = firebase.auth()
db = firebase.database()
storage = firebase.storage()

loggedInUserCategoricalPosts = []

# Create your views here.


def credentials(request):
    global loggedInUserCategoricalPosts
    loggedInUserCategoricalPosts = []

    if "user" in request.session:
        return redirect('home')

    if request.method == "POST":
        if 'username' in request.POST:
            if request.POST['username'] == "":
                try:
                    user = auth.sign_in_with_email_and_password(
                        request.POST['email'], request.POST['password'])

                    uid = user['localId']

                    userCheck = db.child(
                        "users/regularUsers/" + uid).get().val()

                    if(userCheck == None):
                        return JsonResponse({"result": "failure"})

                    user = auth.refresh(user['refreshToken'])

                    user_id = user['idToken']

                    request.session['user'] = user_id
                    request.session['uid'] = uid

                    return JsonResponse({"result": "success"})
                except Exception:
                    return JsonResponse({"result": "failure"})
            else:
                try:
                    try:
                        userCheck = adminAuth.get_user_by_email(
                            request.POST['email'])
                        if userCheck != None:
                            return JsonResponse({"result": "failure", 'userExists': True})
                    except Exception:
                        print()

                    user = auth.create_user_with_email_and_password(
                        request.POST['email'], request.POST['password'])

                    user_id = user['idToken']
                    uid = user['localId']

                    request.session['user'] = user_id
                    request.session['uid'] = uid

                    db.child("users/regularUsers/" + uid).set({
                        "name": request.POST['username'],
                        "email": request.POST['email'],
                        "gender": request.POST['gender'],
                        "description": request.POST['description'],
                        "preferences": {
                            "categories": json.loads(request.POST['selectedCategories'])
                        }
                    })

                    return JsonResponse({"result": "success"})
                except Exception as e:
                    return JsonResponse({"result": "failure"})
        elif 'recoveryEmail' in request.POST:
            try:
                auth.send_password_reset_email(request.POST['recoveryEmail'])

                return JsonResponse({"result": "success"})
            except:
                return JsonResponse({"result": "failure"})

    categories = list()

    categoriesItems = db.child("content/categories").get().val()
    for key, val in categoriesItems.items():
        val["id"] = key
        categories.append(val)

    return render(request, "credentials/credentials.html", {"categories": categories})


def home(request, page_number=0):
    global loggedInUserCategoricalPosts

    loadLimit = 10

    if request.method == "POST":
        if request.POST['type'] == "load":
            try:
                isLoggedIn = "user" in request.session

                loadLimit = 10

                categories = {}
                allPosts = list()

                categoriesItems = db.child("content/categories").get().val()
                for key, val in categoriesItems.items():
                    val["id"] = key
                    categories[key] = val
                categoryIDs = categories.keys()

                if isLoggedIn:
                    userPrefFilter = db.child(
                        "users/regularUsers/" + request.session['uid'] + "/preferences/filterID").get().val()
                    userPrefFilter = int(
                        userPrefFilter) if userPrefFilter != None else 3

                    userTags = db.child(
                        "users/regularUsers/" + request.session['uid'] + "/tags").get().val()
                    userTags = list() if userTags == None else userTags

                    userRecentlyViewed = db.child(
                        "users/regularUsers/" + request.session['uid'] + "/recentlyViewed").get().val()
                    userRecentlyViewed = {} if userRecentlyViewed == None else {
                        item['category']: item['post'] for item in userRecentlyViewed}

                    userPrefCategories = db.child(
                        "users/regularUsers/" + request.session['uid'] + "/preferences/categories").get().val()
                    userPrefCategories = [] if userPrefCategories == None else [
                        k for k, v in userPrefCategories.items()]

                    if len(loggedInUserCategoricalPosts) == 0:
                        allCategoricalPosts = {}
                        for categoryID in userPrefCategories:
                            if categoryID in categoryIDs:
                                categoricalPosts = db.child("content").child("posts").order_by_child(
                                    "category").equal_to(categoryID).get().val()
                                if categoricalPosts != None:
                                    allCategoricalPosts.update(
                                        {k: v for k, v in categoricalPosts.items()})

                        loggedInUserCategoricalPosts = sorted(
                            allCategoricalPosts.items(), reverse=True)
                
                    validPosts = []
                    for currPost in loggedInUserCategoricalPosts:
                        post = dict(currPost[1])
                        postID = currPost[0]
                        post["id"] = postID
                        postTags = post["tags"] if "tags" in post else list()
                        hasCommonTags = False
                        for postTag in postTags:
                            if postTag in userTags:
                                hasCommonTags = True
                                break

                        isRecentlyViewed = postID in userRecentlyViewed.values()
                        hasChance = random.randint(1, 3) % 3 == 0

                        if (userPrefFilter == 0 and hasCommonTags) or (userPrefFilter == 1 and isRecentlyViewed) or (userPrefFilter == 2 and (hasChance or hasCommonTags)) or (userPrefFilter == 3):
                            validPosts.append(post)

                    startAt = page_number * loadLimit

                    selectedPosts = []
                    if(startAt < len(validPosts)):
                        endAt = startAt + loadLimit
                        if(endAt >= len(validPosts)):
                            endAt = len(validPosts)
                        selectedPosts = validPosts[startAt:endAt]
                    else:
                        return JsonResponse({"result": "failure", "invalid": True})

                    for post in selectedPosts:
                        post["category"] = categories[post["category"]]

                        if post['type'] == "article" and post['text'].find("<img") > -1:
                            post['link'] = post['text'][post['text'].find(
                                "<img src=") + 10: post['text'].find("alt") - 2]

                        isLiked = db.child(
                            "likes/" + post['id'] + "/" + request.session['uid']).get().val() != None
                        post["isLiked"] = 1 if isLiked else 0

                        likeStr = "You" if isLiked else ""
                        likes = db.child("likes/" + post['id']).get().val()
                        if likes != None:
                            likes = list(dict(likes).keys())
                            likesCount = len(likes) + \
                                (-1 if isLiked else 0)
                            limit = 2 if isLiked else 3

                            namedUsers = []
                            for i in range(0, limit):
                                if i < len(likes) and likes[i] != request.session['uid']:
                                    namedUsers.append(
                                        db.child("users/regularUsers/" + likes[i] + "/name").get().val())

                            if len(namedUsers) == limit or likesCount - len(namedUsers) <= 0:
                                remainingLikes = likesCount - \
                                    len(namedUsers)

                                for i in range(0, len(namedUsers)):
                                    if i != 0 and i == len(namedUsers) - 1 and remainingLikes == 0:
                                        likeStr += " and "
                                    else:
                                        if not (remainingLikes > 0 and i == len(namedUsers) - 1) and len(likeStr) > 0:
                                            likeStr += ", "

                                    likeStr += namedUsers[i]

                                likeStr += " and " + remainingLikes + \
                                    " other like this" if remainingLikes > 0 else " like this"
                            elif len(namedUsers) == 0:
                                likeStr = "You like this"

                            post["likeStr"] = likeStr

                        else:
                            post["likeStr"] = "Be the first to like this"

                        isSaved = db.child(
                            "users/regularUsers/" + request.session['uid'] + "/saved/" + post['id']).get().val() != None
                        post["isSaved"] = 1 if isSaved else 0

                        comments = list()
                        commentItems = db.child(
                            "comments/" + postID).get().val()
                        if commentItems != None:
                            for key, val in commentItems.items():
                                val["id"] = key

                                val["username"] = "You" if val["userID"] == request.session['uid'] else db.child(
                                    "users/regularUsers/" + val["userID"] + "/name").get().val()

                                comments.append(val)

                        post["allComments"] = comments

                        allPosts.append(post)
                else:
                    posts = {}

                    postIDs = db.child("content/posts").shallow().get().val()

                    startAt = page_number * loadLimit

                    selectedPostIds = []
                    if(postIDs != None and startAt < len(postIDs)):
                        postIDs = list(sorted(postIDs, reverse=True))
                        endAt = startAt + loadLimit
                        if(endAt >= len(postIDs)):
                            endAt = len(postIDs)
                        selectedPostIds = postIDs[startAt:endAt]
                    else:
                        return JsonResponse({"result": "failure", "invalid": True})

                    for postID in selectedPostIds:
                        posts[postID] = dict(
                            db.child("content/posts/" + postID).get().val())

                    for postID in posts:
                        post = posts[postID]
                        post["id"] = postID
                        post["category"] = categories[post["category"]]

                        if post['type'] == "article" and post['text'].find("<img") > -1:
                            post['link'] = post['text'][post['text'].find(
                                "<img src=") + 10: post['text'].find("alt") - 2]

                        post["isLiked"] = 0

                        likeStr = ""
                        likes = db.child("likes/" + postID).get().val()
                        if likes != None:
                            likes = list(dict(likes).keys())
                            likesCount = len(likes)
                            limit = 3

                            namedUsers = []
                            for i in range(0, limit):
                                if i < len(likes):
                                    namedUsers.append(
                                        db.child("users/regularUsers/" + likes[i] + "/name").get().val())

                            if len(namedUsers) == limit or likesCount - len(namedUsers) <= 0:
                                remainingLikes = likesCount - \
                                    len(namedUsers)

                                for i in range(0, len(namedUsers)):
                                    if i != 0 and i == len(namedUsers) - 1 and remainingLikes == 0:
                                        likeStr += " and "
                                    else:
                                        if not (remainingLikes > 0 and i == len(namedUsers) - 1) and len(likeStr) > 0:
                                            likeStr += ", "

                                    likeStr += namedUsers[i]

                                likeStr += " and " + remainingLikes + \
                                    " other like this" if remainingLikes > 0 else " like this"
                            elif len(namedUsers) == 0:
                                likeStr = "Be the first to like this"

                            post["likeStr"] = likeStr

                        else:
                            post["likeStr"] = "Be the first to like this"

                        comments = list()
                        commentItems = db.child(
                            "comments/" + postID).get().val()
                        if commentItems != None:
                            for key, val in commentItems.items():
                                val["id"] = key

                                val["username"] = db.child(
                                    "users/regularUsers/" + val["userID"] + "/name").get().val()

                                comments.append(val)

                        post["allComments"] = comments

                        allPosts.append(post)

                return JsonResponse({"result": "success", "posts": allPosts})
            except Exception as e:
                print(e)
                return JsonResponse({"result": "failure"})
        elif request.POST['type'] == "like":
            try:
                currLikeCount = db.child(
                    "content/posts/" + request.POST["postID"] + "/likes").get().val()
                currLikeCount = 0 if currLikeCount == None else currLikeCount

                currLikeCount += 1 if request.POST["isLike"] == "true" else -1
                db.child("content/posts/" +
                         request.POST["postID"] + "/likes").set(currLikeCount)

                if request.POST["isLike"] == "true":
                    db.child(
                        "likes/" + request.POST["postID"] + "/" + request.session['uid']).set("userID")
                else:
                    db.child(
                        "likes/" + request.POST["postID"] + "/" + request.session['uid']).remove()

                return JsonResponse({"result": "success", "likes": currLikeCount})
            except:
                return JsonResponse({"result": "failure"})
        elif request.POST['type'] == "comment":
            try:
                currCommentsCount = db.child(
                    "content/posts/" + request.POST["postID"] + "/comments").get().val()
                currCommentsCount = 0 if currCommentsCount == None else currCommentsCount

                db.child(
                    "content/posts/" + request.POST["postID"] + "/comments").set(currCommentsCount + 1)

                db.child("comments/" + request.POST["postID"]).push({
                    "comment": request.POST["comment"],
                    "timestamp": int(request.POST["timestamp"]),
                    "userID": request.session['uid']
                })

                return JsonResponse({"result": "success", "comments": currCommentsCount + 1})
            except Exception as e:
                print(e)
                return JsonResponse({"result": "failure"})
        elif request.POST['type'] == "save":
            try:
                if request.POST["isSave"] == "true":
                    db.child("users/regularUsers/" + request.session['uid'] + "/saved/" + request.POST["postID"]).set(
                        request.POST["categoryID"])
                else:
                    db.child("users/regularUsers/" +
                             request.session['uid'] + "/saved/" + request.POST["postID"]).remove()

                return JsonResponse({"result": "success"})
            except Exception as e:
                print(e)
                return JsonResponse({"result": "failure"})

    recentPosts = []
    lastThreePosts = db.child(
        "content/posts").order_by_key().limit_to_last(3).get().val()
    lastThreePosts = dict(lastThreePosts) if lastThreePosts != None else {}

    for postID in lastThreePosts:
        post = lastThreePosts[postID]
        post['id'] = postID

        if post['type'] == "article" and post['text'].find("<img") > -1:
            post['link'] = post['text'][post['text'].find(
                "<img src=") + 10: post['text'].find("alt") - 2]

        recentPosts.append(post)

    recentPosts = sorted(
        recentPosts, key=lambda post: post['id'], reverse=True)

    return render(request, "site/pages/home.html", {"isLoggedIn": "user" in request.session, "pageNumber": page_number, "recentPosts": recentPosts})


def post(request, post_title_id):
    post_title_id = unquote(post_title_id)
    postID = post_title_id.split("_~_")[1]

    post = db.child("content/posts/" + postID).get().val()
    if post != None:
        categories = {}

        categoriesItems = db.child("content/categories").get().val()
        for key, val in categoriesItems.items():
            val["id"] = key
            categories[key] = val
        categoryIDs = categories.keys()

        post = dict(post)
        post["id"] = postID
        post["category"] = categories[post["category"]]

        post["isLiked"] = 0

        likeStr = ""
        likes = db.child("likes/" + postID).get().val()
        if likes != None:
            likes = list(dict(likes).keys())
            likesCount = len(likes)
            limit = 3

            namedUsers = []
            for i in range(0, limit):
                if i < len(likes):
                    namedUsers.append(
                        db.child("users/regularUsers/" + likes[i] + "/name").get().val())

            if len(namedUsers) == limit or likesCount - len(namedUsers) <= 0:
                remainingLikes = likesCount - \
                    len(namedUsers)

                for i in range(0, len(namedUsers)):
                    if i != 0 and i == len(namedUsers) - 1 and remainingLikes == 0:
                        likeStr += " and "
                    else:
                        if not (remainingLikes > 0 and i == len(namedUsers) - 1) and len(likeStr) > 0:
                            likeStr += ", "

                    likeStr += namedUsers[i]

                likeStr += " and " + remainingLikes + \
                    " other like this" if remainingLikes > 0 else " like this"
            elif len(namedUsers) == 0:
                likeStr = "Be the first to like this"

            post["likeStr"] = likeStr

        else:
            post["likeStr"] = "Be the first to like this"

        comments = list()
        commentItems = db.child(
            "comments/" + postID).get().val()
        if commentItems != None:
            for key, val in commentItems.items():
                val["id"] = key

                val["username"] = db.child(
                    "users/regularUsers/" + val["userID"] + "/name").get().val()

                comments.append(val)

        post["allComments"] = comments
    else:
        return redirect("invalid")

    return render(request, "site/pages/post.html", {"isLoggedIn": "user" in request.session, "post": post, "postMap": json.dumps(dict(post))})


def profile(request):
    global loggedInUserCategoricalPosts
    loggedInUserCategoricalPosts = []

    if not "user" in request.session:
        return redirect('home')

    if request.method == "POST":
        if request.POST['type'] == "filter":
            try:
                db.child("users/regularUsers/" +
                         request.session['uid'] + "/preferences/filterID").set(request.POST['filterID'])

                return JsonResponse({"result": "success"})
            except:
                return JsonResponse({"result": "failure"})
        elif request.POST['type'] == "interest":
            try:
                db.child("users/regularUsers/" + request.session['uid'] + "/preferences/categories/" + request.POST['categoryID']).set(
                    None if request.POST['interestOperation'] == "remove" else "categoryID")

                return JsonResponse({"result": "success"})
            except:
                return JsonResponse({"result": "failure"})
        elif request.POST['type'] == "saveProfile":
            try:
                db.child("users/regularUsers/" + request.session['uid']).update({
                    # 'name': request.POST['name'],
                    # 'email': request.POST['email'],
                    'gender': request.POST['gender'] if request.POST['gender'] != "" else None,
                    'description': request.POST['description'] if request.POST['description'] != "" else None
                })

                return JsonResponse({"result": "success", "info": {'gender': request.POST['gender'], 'description': request.POST['description']}})
            except:
                return JsonResponse({"result": "failure"})

    categories = {}

    categoriesItems = db.child("content/categories").get().val()
    for key, val in categoriesItems.items():
        val["id"] = key
        categories[key] = val
    categoryIDs = categories.keys()

    user = db.child("users/regularUsers/" + request.session['uid']).get().val()

    userPrefFilter = 3
    userPrefCategories = []
    userDetails = {
        'name': 'N/A',
        'email': 'N/A',
        'gender': 'N/A',
        'description': "N/A"
    }

    if user != None:
        user = dict(user)

        if 'name' in user:
            userDetails['name'] = user['name']

        if 'email' in user:
            userDetails['email'] = user['email']

        if 'gender' in user:
            userDetails['gender'] = user['gender']

        if 'description' in user:
            userDetails['description'] = user['description']

        userPrefFilter = 3 if 'preferences' not in user else (
            3 if 'filterID' not in user['preferences'] else int(user['preferences']['filterID']))

        userPrefCategories = [] if 'preferences' not in user else ([] if 'categories' not in user['preferences'] else [
                                                                   k for k, v in user['preferences']['categories'].items()])

    interests = list()
    for categoryID in userPrefCategories:
        if categoryID in categoryIDs:
            interests.append(categories[categoryID])

    return render(request, "site/pages/userProfile.html", {"isLoggedIn": "user" in request.session, 'userDetails': userDetails, "categories": categories, "interests": interests, "filterID": userPrefFilter})


def browse(request):
    global loggedInUserCategoricalPosts
    loggedInUserCategoricalPosts = []

    if request.method == "POST":
        if request.POST['type'] == "load":
            try:
                loadLimit = 10

                categories = {}
                allPosts = list()

                categoriesItems = db.child("content/categories").get().val()
                for key, val in categoriesItems.items():
                    val["id"] = key
                    categories[key] = val

                posts = {}
                words = str(request.POST["searchTerm"]).lower().strip().split()
                postIDs = {}
                for word in words:
                    wordPostIDs = db.child("words").child(word).get().val()
                    if wordPostIDs != None:
                        for postID in wordPostIDs.items():
                            postIDs[postID[0]] = "postID"

                postIDs = sorted(postIDs)

                maxPossiblePages = (len(postIDs) % loadLimit) + 1

                correctedPageNumber = maxPossiblePages - \
                    1 if int(request.POST["pageNumber"]) >= maxPossiblePages else int(
                        request.POST["pageNumber"])

                startAt = correctedPageNumber * loadLimit

                hasNext = True

                if len(postIDs) > 0:
                    if startAt >= len(postIDs):
                        hasNext = False
                        startAt = len(postIDs) - loadLimit + 1
                    elif startAt < 0:
                        startAt = 0

                    for i in range(startAt, startAt + loadLimit):
                        if i < len(postIDs):
                            postMap = db.child(
                                "content/posts/" + postIDs[i]).get().val()
                            if postMap != None:
                                posts[postIDs[i]] = dict(postMap)

                    for postID in posts:
                        post = posts[postID]
                        post["id"] = postID
                        post["category"] = categories[post["category"]]

                        isLiked = False if not "user" in request.session else db.child(
                            "likes/" + postID + "/" + request.session['uid']).get().val() != None
                        post["isLiked"] = 1 if isLiked else 0

                        likeStr = "You" if isLiked else ""
                        likes = db.child("likes/" + postID).get().val()
                        if likes != None:
                            likes = list(dict(likes).keys())
                            likesCount = len(likes) + (-1 if isLiked else 0)
                            limit = 2 if isLiked else 3

                            namedUsers = []
                            for i in range(0, limit):
                                if i < len(likes):
                                    if "user" in request.session and likes[i] == request.session['uid']:
                                        continue

                                    namedUserName = db.child(
                                        "users/regularUsers/" + likes[i] + "/name").get().val()
                                    if namedUserName != None:
                                        namedUsers.append(
                                            db.child("users/regularUsers/" + likes[i] + "/name").get().val())

                            if len(namedUsers) == limit or likesCount - len(namedUsers) <= 0:
                                remainingLikes = likesCount - len(namedUsers)

                                for i in range(0, len(namedUsers)):
                                    if i != 0 and i == len(namedUsers) - 1 and remainingLikes == 0:
                                        likeStr += " and "
                                    else:
                                        if not (remainingLikes > 0 and i == len(namedUsers) - 1) and len(likeStr) > 0:
                                            likeStr += ", "

                                    likeStr += namedUsers[i]

                                likeStr += " and " + remainingLikes + \
                                    " other like this" if remainingLikes > 0 else " like this"
                            elif len(namedUsers) == 0:
                                likeStr = "You like this"

                            post["likeStr"] = likeStr

                        else:
                            post["likeStr"] = "Be the first to like this"

                        isSaved = False if not "user" in request.session else db.child(
                            "users/regularUsers/" + request.session['uid'] + "/saved/" + postID).get().val() != None
                        post["isSaved"] = 1 if isSaved else 0

                        comments = list()
                        commentItems = db.child(
                            "comments/" + postID).get().val()
                        if commentItems != None:
                            for key, val in commentItems.items():
                                val["id"] = key

                                if "user" in request.session:
                                    val["username"] = "You" if val["userID"] == request.session['uid'] else db.child(
                                        "users/regularUsers/" + val["userID"] + "/name").get().val()
                                else:
                                    val["username"] = db.child(
                                        "users/regularUsers/" + val["userID"] + "/name").get().val()

                                comments.append(val)

                        post["allComments"] = comments

                        allPosts.append(post)

                    if len(allPosts) < loadLimit:
                        hasNext = False

                allPosts.reverse()

                return JsonResponse({"result": "success", "searchTerm": str(request.POST["searchTerm"]), "posts": allPosts, "pageNum": correctedPageNumber, "pageNumForView": 1 + correctedPageNumber, "hasNext": hasNext, "loadLimit": loadLimit})
            except Exception as e:
                print(e)
                return JsonResponse({"result": "failure", "posts": list()})
        elif request.POST['type'] == "like":
            try:
                currLikeCount = db.child(
                    "content/posts/" + request.POST["postID"] + "/likes").get().val()
                currLikeCount = 0 if currLikeCount == None else currLikeCount

                currLikeCount += 1 if request.POST["isLike"] == "true" else -1
                db.child("content/posts/" +
                         request.POST["postID"] + "/likes").set(currLikeCount)

                if request.POST["isLike"] == "true":
                    db.child(
                        "likes/" + request.POST["postID"] + "/" + request.session['uid']).set("userID")
                else:
                    db.child(
                        "likes/" + request.POST["postID"] + "/" + request.session['uid']).remove()

                return JsonResponse({"result": "success", "likes": currLikeCount})
            except:
                return JsonResponse({"result": "failure"})
        elif request.POST['type'] == "comment":
            try:
                currCommentsCount = db.child(
                    "content/posts/" + request.POST["postID"] + "/comments").get().val()
                currCommentsCount = 0 if currCommentsCount == None else currCommentsCount

                db.child(
                    "content/posts/" + request.POST["postID"] + "/comments").set(currCommentsCount + 1)

                db.child("comments/" + request.POST["postID"]).push({
                    "comment": request.POST["comment"],
                    "timestamp": int(request.POST["timestamp"]),
                    "userID": request.session['uid']
                })

                return JsonResponse({"result": "success", "comments": currCommentsCount + 1})
            except Exception as e:
                print(e)
                return JsonResponse({"result": "failure"})
        elif request.POST['type'] == "save":
            try:
                if request.POST["isSave"] == "true":
                    db.child("users/regularUsers/" + request.session['uid'] + "/saved/" + request.POST["postID"]).set(
                        request.POST["categoryID"])
                else:
                    db.child("users/regularUsers/" +
                             request.session['uid'] + "/saved/" + request.POST["postID"]).remove()

                return JsonResponse({"result": "success"})
            except Exception as e:
                print(e)
                return JsonResponse({"result": "failure"})

    return render(request, "site/pages/browse.html", {"isLoggedIn": "user" in request.session})


def saved(request, page_number=0):
    global loggedInUserCategoricalPosts
    loggedInUserCategoricalPosts = []

    if not "user" in request.session:
        return redirect('home')

    loadLimit = 10

    categories = {}
    allPosts = list()

    categoriesItems = db.child("content/categories").get().val()
    for key, val in categoriesItems.items():
        val["id"] = key
        categories[key] = val

    userSavedPostIDs = db.child(
        "users/regularUsers/" + request.session['uid'] + "/saved").get().val()
    userSavedPostIDs = {} if userSavedPostIDs == None else {
        k: v for k, v in userSavedPostIDs.items()}

    tempUserSavedPostIDs = {}
    categoryIDs = categories.keys()
    for postID in userSavedPostIDs:
        if userSavedPostIDs[postID] in categoryIDs:
            tempUserSavedPostIDs[postID] = userSavedPostIDs[postID]

    userSavedPostIDs = tempUserSavedPostIDs

    userSavedPostIDsSorted = sorted(userSavedPostIDs.items(), reverse=True)

    maxPossiblePages = (len(userSavedPostIDsSorted) % loadLimit) + 2

    if page_number >= maxPossiblePages:
        return redirect("invalid")

    correctedPageNumber = maxPossiblePages - \
        1 if page_number >= maxPossiblePages else page_number

    startAt = correctedPageNumber * loadLimit

    hasNext = True

    if len(userSavedPostIDsSorted):
        if startAt >= len(userSavedPostIDsSorted):
            hasNext = False
            startAt = len(userSavedPostIDsSorted) - loadLimit + 1
        elif startAt < 0:
            startAt = 0

        for i in range(startAt, startAt + loadLimit):
            if i < len(userSavedPostIDsSorted):
                postID = userSavedPostIDsSorted[i][0]
                post = db.child("content/posts/" + postID).get().val()
                if post == None:
                    break
                post = dict(post)
                post["id"] = postID
                post["category"] = categories[userSavedPostIDs[postID]]

                allPosts.append(post)

        if len(allPosts) < loadLimit:
            hasNext = False

    return render(request, "site/pages/savedPosts.html", {"isLoggedIn": "user" in request.session, "posts": allPosts, "pageNum": correctedPageNumber, "pageNumForView": 1 + correctedPageNumber, "hasNext": hasNext})


def comingSoon(request):
    return render(request, "coming_soon.html", {"isLoggedIn": "user" in request.session})


def invalid(request):
    return render(request, "invalid.html")


def logout(request):
    global loggedInUserCategoricalPosts
    loggedInUserCategoricalPosts = []

    request.session.pop('user', None)
    request.session.pop('uid', None)

    return redirect("home")
