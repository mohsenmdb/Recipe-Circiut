package com.me.recipe.data.features.auth.mapper

import com.me.recipe.data.core.utils.mappers.NullableInputMapper
import com.me.recipe.domain.features.auth.model.Register
import com.me.recipe.domain.features.auth.model.User
import com.me.recipe.network.features.recipe.model.RegisterDto
import javax.inject.Inject

class RegisterDtoMapper @Inject constructor() :
    NullableInputMapper<RegisterDto, Register> {
    override fun map(input: RegisterDto?): Register {
        return Register(
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
