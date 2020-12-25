package xyz.lrhm.komakdast.core.data.source.local.db

import androidx.room.TypeConverter
import xyz.lrhm.komakdast.core.data.model.Lesson

class TypeConverter {

    @TypeConverter
    fun strToLessonType(string: String) = Lesson.Type.valueOf(string)

    @TypeConverter
    fun lessonTypeToStr(type: Lesson.Type) = type.type
}