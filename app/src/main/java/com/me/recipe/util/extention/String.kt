package com.me.recipe.util.extention

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.encodeToUtf8(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
