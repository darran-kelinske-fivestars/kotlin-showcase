package client

import client.component.FileBackupComponent
import client.component.UndoComponent
import client.util.LocalStorageRepository
import common.*
import common.util.*
import net.yested.ext.jquery.yestedJQuery

/**
 * Persistent Repositories.
 * @author Eric Pabst (epabst@gmail.com)
 * Date: 6/9/16
 * Time: 6:19 AM
 */

object Factory {
    val toDoRepository: Repository<ToDo> = if (true) ToDoLocalStorageRepository() else InMemoryRepository()
    val allRepositories = listOf(toDoRepository)

    init {
        if (toDoRepository is ToDoLocalStorageRepository) {
            if (!toDoRepository.isInitialized()) {
                yestedJQuery.get<Any>("initial-data.json") { initialData ->
                    FileBackupComponent.initializeData(initialData)
                }
            }
        }

        UndoComponent.watch(toDoRepository)
    }
}

open class ToDoLocalStorageRepository : LocalStorageRepository<ToDo, ToDoJS>("toDoList", { it.toNormal() })
