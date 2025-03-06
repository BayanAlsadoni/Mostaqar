package com.example.mostaqarapp.data

import com.example.mostaqarapp.R

class CategoryData(val categoryImage:Int, val categoryName:String)

class HomeDetails(
    var garage: Int? = 0,
    var room: Int? = 0,
    var bathroom: Int? = 0,
    var space:Float? = 0f,
    )

class ProfileData(
    val icon:Int,
    val name:String,
    val image:Int,
    var text:String = "لدينا سابقة أعمال مشرفة. لدينا فريق متميز من أمهر المهندسين والاستشاريين  في مجال العقارات. تعد شركتنا من أكبر الشركات الرائدة في مجال  الاستثمار العقاري المصري. تمتلك الشركة تاريخ حافل بالانجازات والمشروعات  الناجحة. نتميز في الشركة بالالتزام بتسليم المشروعات وفق  الجدول الزمني المتفق عليه. من أبرز أسرار نجاح الشركة هو الالتزام بمواصفات  الجودة العالمية في البناء. نظرًا إلى التعاون مع نخبة من أمهر الخبراء في مجال  الاستثمار العقاري، استطاعت الشركة أن تكسب ثقة  شريحة كبيرة من العملاء. لدينا خبرة طويلة تتجاوز 10 سنوات في مجال  العقارات. هدفنا الأساسي هو إرضاء العملاء وتقديم خدمات  مميزة تنال إعجابهم. تقدم الشركة خيارات عقارية متنوعة، إلى جانب الأسعار  التنافسية وطرق السداد الميسرة. الاهتمام باختيار موقع المشروعات العقارية من أبرز  أسرار نجاح الاستثمار. نحرص في مشروعاتنا على توفير الخدمات المتكاملة  التي تخدم العملاء وتحقق لهم الاستقرار. من أهم ما يميز مشروعات شركتنا الاهتمام بالمساحات  الخضراء والمرافق الخدمية المتنوعة."
)


data class UsersMessageData (
    var uid:String = "",
    var senderImage: String="",
    var textMessage : String = "",
    var senderName: String="",
    var timestamp: String="", )

data class CommentMessage (
    var senderUid:String="",
    var senderName:String="",
    var message:String="",
    var timeStamp:String="",
    var imgProfile:String= ""
)

data class NotificationData(var image: Int= R.drawable.baseline_notifications_24, var notText:String="", var notTime:String="")


