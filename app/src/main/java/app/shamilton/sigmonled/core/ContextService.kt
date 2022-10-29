package app.shamilton.sigmonled.core

import android.content.Context
import com.badoo.reaktive.subject.behavior.BehaviorSubject

object ContextService {
    var context: Context?
        get() = onContextReceived.value
        set(value) {
            if(value == null)
                throw NullPointerException("Context cannot be set to null!")
            onContextReceived.onNext(value)
        }
    val onContextReceived = BehaviorSubject<Context?>(null)
}