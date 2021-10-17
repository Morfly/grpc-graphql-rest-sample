package io.morfly.client.graphql

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant


class TimestampScalarAdapter : CustomTypeAdapter<Instant> {
    override fun decode(value: CustomTypeValue<*>): Instant =
        value.value.toString().toInstant()

    override fun encode(value: Instant): CustomTypeValue<*> =
        CustomTypeValue.GraphQLString(value.toString())
}