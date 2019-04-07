package com.byt.eem.model

import com.google.gson.annotations.SerializedName

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/7 11:46 PM
 * Description :
 */
data class MyProject(
        @SerializedName("Id") val id: Float,
        @SerializedName("TProjectName") val projectName: String
)