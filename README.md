# MediaPlayerByKotlin Kt音樂播放器
**製作目的 :** 
<br>剛讀完一個kotlin的書,想說做個APP順便練習kotlin,
<br>剛好換了新手機,以前ASUS有個撥放音樂軟體不錯
<br>可是新的ASUS沒有,也沒找到其他不錯的播放器
<br>所以就順手寫了一個。

**心得 :**
<br>一開始以為只是個音樂播放器應該是沒有什麼太大的難度，會比較有困難的可能是第一次使用Kotlin不習慣的問題，
<br>但是沒有想到MediaPlayer的整個生命週期比想像中的還要複雜，Service在Android 8.0之後又有不同的規定，例如:
<br>8.0後強制規定要綁一個Notification告知使用者有一個Service在後端運作，而且還要在運行的五秒內進行綁定完成，否則APP會閃退。
<br>而在取出手機裡面的音樂檔時，例如:音樂檔的縮圖、名稱、撥放長度...，才發現有ContentProvider可以使用。
<br>而在Room的操作方面則是在外來鍵和每次的開啟的檢查更新，還有新增、修改、刪除如何與UI互動上較為困難。



# google play:kt音樂播放器  
[google play kt音樂播放器 ](https://play.google.com/store/apps/details?id=com.shang.mediaplayerbykotlin   "kt音樂播放器 ")

![QRCODE](https://github.com/CiaShangLin/MediaPlayerByKotlin/blob/master/180731231505.png "QRCODE")  

# 主要使用元件:  
    1.AppBarLayout+CollapsingToolbarLayout+ToolBar
    2.Recyclerview+CardView 
    3.Room+LiveData+ViewModel
    4.Service+Broadcast+Notification  
    5.MediaPlayer+MediaStore
    6.Drawerlayout+Navigation View 

# APP瀏覽圖  
![][圖片/unnamed (1).jpg]