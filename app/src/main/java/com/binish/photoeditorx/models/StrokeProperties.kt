package com.binish.photoeditorx.models

class StrokeProperties {
    var color: Int = 0
    var width: Float = 0f

    var innerShadow: Shadow = Shadow()

    var outerShadow: Shadow = Shadow()

    constructor(width: Float, color:Int){
        this.width = width
        this.color = color
    }

    constructor(shadow: Shadow, shadowType: ShadowType){
        if(shadowType == ShadowType.INNER)
            this.innerShadow = shadow
        else if(shadowType == ShadowType.OUTER)
            this.outerShadow = shadow
    }

    inner class Shadow{
        var r: Float = 0f
        var dx: Float = 0f
        var dy: Float = 0f
        var color: Int = 0
    }

    enum class ShadowType{
        INNER, OUTER
    }
}