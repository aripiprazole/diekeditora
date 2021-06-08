package com.diekeditora.infra.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager

// The below code is just a copy-paste that should actually be in the lib
class BatchInsertUpdateOnDuplicate(table: Table, val onDupUpdate: List<Column<*>>) :
    BatchInsertStatement(table, false) {
    override fun prepareSQL(transaction: Transaction): String {
        val onUpdateSQL = buildString {
            if (onDupUpdate.isNotEmpty()) {
                append(" ON CONFLICT ")
                append(
                    onDupUpdate.joinToString(prefix = "(", postfix = ")") {
                        transaction.identity(it)
                    }
                )
                append(" DO UPDATE SET  ")
            }
        }

        return super.prepareSQL(transaction) + onUpdateSQL
    }
}

fun <T : Table, E> T.insertOrUpsert(
    data: List<E>,
    onDupUpdateColumns: List<Column<*>>,
    body: T.(BatchInsertUpdateOnDuplicate, E) -> Unit
) {
    data.takeIf { it.isNotEmpty() }?.let {
        val insert = BatchInsertUpdateOnDuplicate(this, onDupUpdateColumns)
        data.forEach {
            insert.addBatch()
            body(insert, it)
        }

        TransactionManager.current().exec(insert)
    }
}
