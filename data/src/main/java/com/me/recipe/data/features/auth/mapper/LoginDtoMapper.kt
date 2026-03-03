package com.me.recipe.data.features.auth.mapper

import com.me.recipe.data.core.utils.mappers.NullableInputMapper
import com.me.recipe.domain.features.auth.model.Login
import com.me.recipe.domain.features.auth.model.User
import com.me.recipe.network.features.recipe.model.LoginDto
import javax.inject.Inject

class LoginDtoMapper @Inject constructor() :
    NullableInputMapper<LoginDto, Login> {
    override fun map(input: LoginDto?): Login {
        return Login(
            accessToken = input?.data?.accessToken.orEmpty(),
            user = User(
                username = input?.data?.user?.username.orEmpty(),
                firstName = input?.data?.user?.firstName.orEmpty(),
                lastName = input?.data?.user?.lastName.orEmpty(),
                age = input?.data?.user?.age,
            ),
        )
    }
}
