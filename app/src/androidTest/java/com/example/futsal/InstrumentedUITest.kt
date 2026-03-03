package com.example.futsal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.futsal.view.LoginBody
import com.example.futsal.view.RegistrationBody
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_isDisplayed() {
        // Start the app with LoginBody
        composeTestRule.setContent {
            LoginBody()
        }

        // Check if "Welcome Back" text is displayed
        composeTestRule.onNodeWithText("Welcome Back").assertIsDisplayed()
        
        // Check if "Email" placeholder is displayed
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        
        // Check if "Log In" button text is displayed
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
    }

    @Test
    fun registrationScreen_isDisplayed() {
        // Start the app with RegistrationBody
        composeTestRule.setContent {
            RegistrationBody()
        }

        // Check if the "Welcome" text is displayed on Registration screen
        composeTestRule.onNodeWithText("Welcome").assertIsDisplayed()
        
        // Check if "Email" placeholder is displayed
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        
        // Check if "Sign Up" button text is displayed
        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()
    }
}
