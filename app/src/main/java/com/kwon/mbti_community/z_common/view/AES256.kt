package com.kwon.mbti_community.z_common.view
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

var iv = byteArrayOf(
    0x01,
    0x02,
    0x03,
    0x04,
    0x05,
    0x06,
    0x07,
    0x08,
    0x09,
    0x10,
    0x11,
    0x12,
    0x13,
    0x14,
    0x15,
    0x16
)

// 사용자 지정 키로 AES256 암호화
@Throws(Exception::class)
fun encByKey(key: String, value: String): String? {
    return encByKey(key.toByteArray(), value.toByteArray())
}

// 사용자 지정 키로 AES256 복호화
@Throws(Exception::class)
fun encByKey(key: ByteArray?, value: ByteArray?): String? {
    val secretKeySpec = SecretKeySpec(key, "AES")
    val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
    val randomKey: ByteArray = cipher.doFinal(value)
    return Base64.encodeToString(randomKey, 0)
}

// 사용자 지정 키로 AES256 복호화
@Throws(Exception::class)
fun decByKey(key: String, plainText: String?): String? {
    return decByKey(key.toByteArray(), Base64.decode(plainText, 0))
}

// 사용자 지정 키로 AES256 복호화
@Throws(Exception::class)
fun decByKey(key: ByteArray?, encText: ByteArray?): String? {
    val secretKeySpec = SecretKeySpec(key, "AES")
    val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
    val secureKey: ByteArray = cipher.doFinal(encText)
    return String(secureKey)
}