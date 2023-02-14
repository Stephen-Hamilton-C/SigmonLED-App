package app.shamilton.sigmonled.ui.bottombar

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.core.bluetooth.DeviceManagerViewModel
import app.shamilton.sigmonled.ui.scaffold.AppScaffold
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.publish.PublishSubject

object AppBottomBar {
    var currentTab: BottomTab? = null
        private set(value) {
            if(field != value) {
                field = value
                onTabSelected.onNext(value)
            }
        }
    var onTabSelected = PublishSubject<BottomTab?>()
    
    @Composable
    fun Component(deviceManager: DeviceManager) {
        val viewModel: DeviceManagerViewModel =
            viewModel(factory = DeviceManagerViewModel.Factory(deviceManager))

        var currentTabIndex by rememberSaveable { mutableStateOf(0) }
        var currentPage by rememberSaveable { mutableStateOf(AppScaffold.currentPage) }
        AppScaffold.onPageNavigation.subscribe {
            if(currentPage != it) {
                currentPage = it
                currentTabIndex = 0
            }
        }

        val tabs = BottomTab.values().filter { it.parentPage == currentPage }

        fun isTabEnabled(tab: BottomTab) = !tab.disableOnDisconnect || viewModel.isConnected

        fun validateTabs() {
            tabs[currentTabIndex].let { activeTab ->
                if(isTabEnabled(activeTab)) {
                    currentTab = activeTab
                } else {
                    currentTab = tabs.find { isTabEnabled(it) }
                    currentTabIndex = tabs.indexOf(currentTab)
                }
            }
        }
        deviceManager.onDeviceDisconnected.subscribe { validateTabs() }

        if(tabs.isNotEmpty()) {
            validateTabs()

            BottomAppBar {
                BottomNavigation() {
                    for((i, tab) in tabs.withIndex()) {
                        BottomNavigationItem(
                            selected = tab == currentTab,
                            icon = { Icon(tab.icon, tab.displayName) },
                            label = { Text(tab.displayName) },
                            enabled = isTabEnabled(tab),
                            onClick = {
                                currentTabIndex = i
                                currentTab = tab
                            },
                        )
                    }
                }
            }
        } else {
            currentTab = null
            currentTabIndex = 0
        }
    }
}