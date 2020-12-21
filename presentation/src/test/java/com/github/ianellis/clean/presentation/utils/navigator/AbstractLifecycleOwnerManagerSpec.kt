package com.github.ianellis.clean.presentation.utils.navigator

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

class AbstractLifecycleOwnerManagerSpec {

    internal class TestLifecycleOwner(
        private val lifecycle: Lifecycle = mockk(relaxed = true)
    ) : LifecycleOwner {
        override fun getLifecycle(): Lifecycle {
            return lifecycle
        }
    }

    internal class TestLifecycleOwnerManager : AbstractLifecycleOwnerManager<TestLifecycleOwner>(
        TestLifecycleOwner::class
    ) {
        fun publicOwner(): TestLifecycleOwner? = this.owner
    }

    internal class WeakTestLifecycleOwnerManager(
        factory: (LifecycleOwner?) -> WeakReference<LifecycleOwner?>
    ) : AbstractLifecycleOwnerManager<TestLifecycleOwner>(
        TestLifecycleOwner::class,
        factory
    ) {
        fun publicOwner(): TestLifecycleOwner? = this.owner
    }

    private lateinit var manager: TestLifecycleOwnerManager

    @Before
    fun setup() {
        manager = TestLifecycleOwnerManager()
    }

    @Test
    fun `owner - is null initially`() {
        manager.publicOwner() shouldBeEqualTo null
    }

    @Test
    fun `onStart() - sets lifecycle owner`() {
        val owner = TestLifecycleOwner()
        manager.onStart(owner)
        manager.publicOwner() shouldBeEqualTo owner
    }

    @Test
    fun `onStart() - throws IllegalStateException when owner is not of specified type`() {
        val owner = mockk<LifecycleOwner>()
        invoking { manager.onStart(owner) } shouldThrow IllegalStateException::class
    }

    @Test
    fun `owner - is null if no reference is kept`() {
        // given we are storing the references externally, so we can clear them
        val created = mutableListOf<WeakReference<LifecycleOwner?>>()
        val manager = WeakTestLifecycleOwnerManager {
            val ref = WeakReference(it)
            created.add(ref)
            ref
        }
        // when we set the owner
        val owner = TestLifecycleOwner()
        manager.onStart(owner)
        // then owner is available
        manager.publicOwner() shouldBeEqualTo owner
        // then it is de-referenced
        created.first().clear()
        // then we do not have access to the owner
        manager.publicOwner() shouldBeEqualTo null
    }

    @Test
    fun `onDestroy() - clear references to lifecycleOwner`() {
        val owner = TestLifecycleOwner()
        manager.onStart(owner)
        manager.publicOwner() shouldBeEqualTo owner
        manager.onDestroy(owner)
        manager.publicOwner() shouldBeEqualTo null
    }

    @Test
    fun `onDestroy() removes itself as listener from lifecycle`() {
        val lifecycle = mockk<Lifecycle>(relaxed = true)
        val owner = TestLifecycleOwner(lifecycle)
        manager.onDestroy(owner)
        verify { lifecycle.removeObserver(manager) }
    }
}
