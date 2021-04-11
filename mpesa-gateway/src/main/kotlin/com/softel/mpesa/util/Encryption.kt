package com.softel.mpesa.util

import com.softel.mpesa.enums.EnvironmentProfile
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

import java.security.PublicKey
import java.security.Security
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

import javax.crypto.Cipher

object MpesaEncryption {

    fun encryptInitiatorPassword(password: String, activeProfile: String): String {
        Security.addProvider(BouncyCastleProvider())
        val resource: Resource              = getCertificateResource(activeProfile)
        val input                           = password.toMpesaByteArray()
        val cipher: Cipher                  = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC")
        val fileInputStream                 = resource.inputStream
        val certificateFactory              = CertificateFactory.getInstance("X.509")
        val certificate: X509Certificate    = certificateFactory.generateCertificate(fileInputStream) as X509Certificate
        val publicKey: PublicKey            = certificate.publicKey

        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val cipherText: ByteArray = cipher.doFinal(input)

        return Helper.encodeToBase64String(cipherText)
    }

    private fun getCertificateResource(activeProfile: String): Resource {
        return when(EnvironmentProfile.valueOf(activeProfile.toUpperCase())) {
            EnvironmentProfile.DEV   -> ClassPathResource("mpesa-certificate/sandbox.mabura.zeguru.cer")
            EnvironmentProfile.UAT   -> ClassPathResource("mpesa-certificate/sandbox.mabura.zeguru.cer")
            EnvironmentProfile.PROD  -> ClassPathResource("mpesa-certificate/prod.mabura.zeguru.cer")
        }
    }
}