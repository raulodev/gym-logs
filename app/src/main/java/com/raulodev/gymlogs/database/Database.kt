package com.raulodev.gymlogs.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update


@Entity
data class Gym(
    @PrimaryKey(autoGenerate = true) val gymId: Int = 0, @ColumnInfo(name = "name") val name: String
)

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "gender") val gender: String?
)

@Entity
data class Payment(
    @PrimaryKey(autoGenerate = true) val paymentId: Int = 0,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "paymentOwnerId") val paymentOwnerId: Int
)


data class UserAndCurrentPaymentDataClass(
    @Embedded val user: User,
    @Embedded val payment: Payment?,
)

data class GenderCountDataClass(
    val male: Int,
    val female: Int,
    val unknown: Int,
    val total: Int
)

data class PaymentDataClass(
    val month: Int,
    val payments: Int
)

@Dao
interface GymDao {
    @Query("SELECT * FROM gym")
    fun getAll(): List<Gym>

    @Query("SELECT COUNT(*) FROM gym")
    fun count(): Int

    @Insert
    suspend fun insert(vararg gym: Gym)
}

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE userId IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query(
        """
    SELECT
        SUM(CASE WHEN gender = 'Male' THEN 1 ELSE 0 END) AS male,
        SUM(CASE WHEN gender = 'Female' THEN 1 ELSE 0 END) AS female,
        SUM(CASE WHEN gender = 'Unknown' THEN 1 ELSE 0 END) AS unknown,
        COUNT(*) AS total
    FROM user
    """
    )
    suspend fun countUsersByOwner(): GenderCountDataClass

    @Update
    suspend fun updateUser(user: User)

    @Insert
    suspend fun insert(vararg user: User)


    @Query(
        """
    SELECT *
    FROM user 
    LEFT OUTER JOIN payment 
        ON user.userId = payment.paymentOwnerId 
        AND payment.month = CAST(strftime('%m' , 'now') AS INTEGER)
        AND payment.year = CAST(strftime('%Y' , 'now') AS INTEGER)
        
    ORDER BY
        CASE WHEN :isAsc = 1 THEN LOWER(user.name) END ASC,
        CASE WHEN :isAsc = 0 THEN LOWER(user.name) END DESC
    """
    )
    suspend fun getAllUsersAndCurrentPayment(isAsc: Boolean = true): List<UserAndCurrentPaymentDataClass>


    @Delete
    fun delete(user: User)
}

@Dao
interface PaymentsDao {

    @Insert
    suspend fun insert(vararg payment: Payment)

    @Delete
    suspend fun delete(vararg payment: Payment)


    @Query("""SELECT month , COUNT(*) as payments FROM payment WHERE year = :year GROUP BY month ORDER BY month;""")
    suspend fun getByMonth(year: Int): List<PaymentDataClass>

}

@Database(entities = [User::class, Gym::class, Payment::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gymDao(): GymDao
    abstract fun paymentDao(): PaymentsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
