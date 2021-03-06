package xyz.lrhm.komakdast.core.util

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.model.Package
import xyz.lrhm.komakdast.core.data.source.AppRepository
import xyz.lrhm.komakdast.core.util.models.LocalLessonList
import xyz.lrhm.komakdast.core.util.models.LocalPackageList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUtil @Inject constructor(
    val moshi: Moshi,
    @ApplicationContext val context: Context,
    val repo: AppRepository
) {

    suspend fun syncLocalPackagesWithDB() {

        val packages = repo.getPackages()

        val embeddedPackages = parseLocalPackageList()


        for (p in embeddedPackages) {

            val hit = packages.find { it.id == p.id }

            if (hit?.revision != p.revision) {

                var lastResolvedId: Int? = null
                if (hit != null) {

                    lastResolvedId = repo.localDataSource.clearDb(hit.id)
                }

                insertPackage(p, lastResolvedId)
            }

        }


    }

    suspend fun insertPackage(p: Package, lastResolvedId: Int?) {

        val levels = parseLevels(p.id)
        var order = 0;
        for (l in levels) {
            l.packageId = p.id
            l.order = order
            order++

            if (lastResolvedId != null && l.id <= lastResolvedId) {
                l.resolved = true
            }
        }

        repo.insertPackage(p, levels)


    }

    fun parseLocalPackageList(): List<Package> {

        try {
            val string = assetFileToString("local.json")

            val converter = moshi.adapter(LocalPackageList::class.java)
//            Timber.d("string is $string")
            return converter.fromJson(string)!!.objects

//            Timber.d("object is $objects")
//
//            parseLevels()


        } catch (e: Exception) {
            e.printStackTrace()
        }

        return emptyList()


    }

    fun parseLevels(packageId: Int): List<Lesson> {


        val packagePath = "Packages/package_$packageId"
        val jsonFile = "${packagePath}/levels.json"
        val levels = parseLevelList(jsonFile)

        return levels


    }

    fun parseLevelList(path: String): List<Lesson> {

        val str = assetFileToString(path)

        val converter = moshi.adapter(LocalLessonList::class.java)

        val levels = converter.fromJson(str)

        return levels!!.levels


    }

    fun assetFileToString(path: String): String {

//        CoroutineScope(Dis)

        val inputStream = context.assets.open(path)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        return String(buffer)
    }


}