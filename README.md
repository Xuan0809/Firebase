# Firebase
Include FB / Google Login &amp; FireStore DB

ref : https://medium.com/chikuwa-tech-study/android-%E4%BD%BF%E7%94%A8firebase%E5%AF%A6%E7%8F%BEfacebook%E7%99%BB%E5%85%A5-8a6ba75ad348

FB part with Firebase :

1.至 https://console.firebase.google.com/ 建立 Firebase 帳戶 , 並建立專案
  至 https://developers.facebook.com/?no_redirect=1 建立 Facebook Devloper 帳戶 , 並建立專案

2.參考 ref 將 FB 專案 應用程式 ID  & 應用程式密鑰 bundle 到 FireBase Authentication / Sign-in providers / Facebook
  並將此頁面的 OAuth 網址 bundle 到 Facebook 專案 / Facebook 登入 : 設定 ／ 用戶端 OAuth 設定 : 有效的 OAuth 重新導向 URI 下

3.根據兩邊的開發者網站教學至 build.grade 加入 ref & implementation
  至 values.string 加入 "FBID"
  至 manifests 加入 FB 登入詢問畫面

** Note : 11/23 5 pm ~ 11/24 10.30 am 發生 token 失效 無法登入問題 , google 各解決方式無效

Google part with Firebase :

1.至 https://console.firebase.google.com/ 建立 Firebase 帳戶 , 並建立專案 ( 經過 FB 階段已建立好)
  -> Sign-in providers Enable : Google 

2.至 Firebase : Project settings / Your Apps : Android apps
  針對專案新增 SHA certificate fingerprints key

3.根據 https://firebase.google.com/docs/auth/android/google-signin 新增 ref & implementation

Function part :

FB : LoginButton OnClick : Ask Token -> if Token != null -> FacebookSignIn()

Google : LoginButton OnClick : GoogleRegister() -> ( onActivityResult ) if requestCode same 
         -> Ask Token ->  -> if Token != null -> GoogleSignIn()

