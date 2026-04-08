package com.url.androidcore.core.security

import java.security.MessageDigest
import android.util.Base64

/**
 * Encryption interface for encode/decode operations.
 *
 * Template for AI agents: Replicate this pattern for other crypto needs
 * (Hashing, Signing, TokenEncryption, etc.)
 *
 * Implementations: Base64Encryption (simple), AesEncryption (advanced)
 */
interface Encryption {

    /**
     * Encode plain text to encrypted/encoded string.
     * Agents template: Use for data protection at rest.
     */
    fun encode(plainText: String): String

    /**
     * Decode encrypted/encoded string back to plain text.
     * Agents template: Use for data retrieval/decryption.
     */
    fun decode(encodedText: String): String
}

/**
 * Base64 encoding implementation (no encryption, just encoding).
 *
 * Agents template: Shows simple Encryption implementation.
 * WARNING: Base64 is NOT encryption. Use only for obfuscation, not sensitive data.
 */
class Base64Encryption : Encryption {

    override fun encode(plainText: String): String {
        return Base64.encodeToString(plainText.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    }

    override fun decode(encodedText: String): String {
        return String(Base64.decode(encodedText, Base64.NO_WRAP), Charsets.UTF_8)
    }
}

/**
 * Hash utility object for cryptographic hashing.
 *
 * Template for AI agents: Extend for other hash algorithms (BLAKE2, Argon2, etc.)
 * Agents template: Use for password hashing, data integrity verification.
 */
object Hash {

    /**
     * SHA-256 hash algorithm.
     * Agents template: Standard for security-sensitive operations.
     */
    fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * MD5 hash algorithm (DEPRECATED for security, use SHA-256).
     * Agents template: Use only for checksums, NOT for security.
     */
    fun md5(input: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Simple password hash with salt.
     * Agents template: Pattern for salted hashing.
     * Note: For production, use bcrypt or Argon2.
     */
    fun hashWithSalt(input: String, salt: String = ""): String {
        return sha256(input + salt)
    }

    /**
     * Verify input against hash (useful for password validation).
     * Agents template: Pattern for hash verification.
     */
    fun verify(input: String, hash: String, salt: String = ""): Boolean {
        return hashWithSalt(input, salt) == hash
    }
}

/**
 * Simple key-value storage for security-sensitive data.
 *
 * Agents template: Pattern for managing secrets/tokens.
 * Note: This is in-memory. For persistent secure storage, use EncryptedSharedPreferences.
 */
class SecureStorage {

    private val storage = mutableMapOf<String, String>()

    /**
     * Store encrypted data.
     * Agents template: Use for API tokens, refresh tokens, etc.
     */
    fun put(key: String, value: String, encrypt: Boolean = true) {
        val storedValue = if (encrypt) {
            Base64Encryption().encode(value)
        } else {
            value
        }
        storage[key] = storedValue
    }

    /**
     * Retrieve and decrypt data.
     * Agents template: Safe data retrieval pattern.
     */
    fun get(key: String, decrypt: Boolean = true): String? {
        return storage[key]?.let { encrypted ->
            if (decrypt) {
                try {
                    Base64Encryption().decode(encrypted)
                } catch (e: Exception) {
                    null
                }
            } else {
                encrypted
            }
        }
    }

    /**
     * Remove data from storage.
     * Agents template: Cleanup pattern.
     */
    fun remove(key: String) {
        storage.remove(key)
    }

    /**
     * Clear all stored data.
     * Agents template: Reset pattern (e.g., on logout).
     */
    fun clear() {
        storage.clear()
    }

    /**
     * Check if key exists.
     * Agents template: Presence check pattern.
     */
    fun contains(key: String): Boolean = storage.containsKey(key)
}

/**
 * Extension function to hash strings with SHA-256.
 * Agents template: Convenient hashing on string type.
 *
 * Usage:
 * ```kotlin
 * val hashedPassword = "myPassword".sha256()
 * ```
 */
fun String.sha256(): String = Hash.sha256(this)

fun String.md5(): String = Hash.md5(this)

/**
 * Extension function to encode/decode strings.
 * Agents template: Convenient encoding on string type.
 *
 * Usage:
 * ```kotlin
 * val encoded = "secret".encodeBase64()
 * val decoded = encoded.decodeBase64()
 * ```
 */
fun String.encodeBase64(): String = Base64Encryption().encode(this)

fun String.decodeBase64(): String = Base64Encryption().decode(this)

