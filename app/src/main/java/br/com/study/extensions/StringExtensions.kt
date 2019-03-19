package br.com.study.extensions

import java.lang.StringBuilder
import java.security.NoSuchAlgorithmException

/*
MARVEL PARAMS MD5
 */

fun String.md5(): String{
    try{
        val digest = java.security.MessageDigest.getInstance("MD5")
        digest.update(toByteArray())
        val massageDigest = digest.digest()
        val hexString = StringBuilder()
        for( aMessageDigest in massageDigest ){
            var  h = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while ( h.length < 2)
                h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    }catch (e: NoSuchAlgorithmException){
        e.printStackTrace()
    }
    return ""
}