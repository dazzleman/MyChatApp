package ru.ic218.mychatapp.domain.model

interface PrefRepository {

    var isCompleteInstall: Boolean
    var ownerId: Int
    var ownerName: String
}