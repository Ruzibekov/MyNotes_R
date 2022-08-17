package com.ruzibekov.mynotes_r.room

import androidx.room.*

@Database(entities = [NoteModel::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
     abstract fun noteDao(): NoteDao
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteModel")
    fun getAllNotes(): List<NoteModel>

    @Insert
    fun insertNote(newNote: NoteModel)

    @Query("UPDATE NoteModel SET text=:text, title=:title WHERE id=:id")
    fun updateNote(title: String, text: String, id: Int)

    @Delete
    fun delete(noteModel: NoteModel)
}

@Entity
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val text: String,
)