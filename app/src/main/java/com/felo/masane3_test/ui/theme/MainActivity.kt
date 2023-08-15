package com.felo.masane3_test.ui.theme

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.felo.masane3_test.R
import com.felo.masane3_test.data.state.DataState
import com.felo.masane3_test.viewModel.AuthViewModel
import com.felo.masane3_test.viewModel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "Felooooooooooooo"



    private val viewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            Masane3_testTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
        /*viewModel.loginResponse?.observe(this){
            Log.d(TAG, "observe: ")
        }*/
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @Preview(showSystemUi = true)
    fun LoginScreen()
    {
        val uiState by viewModel.loginUIState.collectAsState()
        Scaffold(
            topBar = {
                GenericAppBar(
                    title = "Login",
                    onNavigationIconClick = {}
                )
            }
        ) {paddingValues ->
            Column(
               modifier = Modifier
                   .padding(paddingValues)
                   .fillMaxSize(),

            ){
                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Mobile",
                    color = Color.Black,
                    modifier = Modifier.padding(start = 12.dp),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 9.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    shape = RoundedCornerShape(20.dp)
                ){
                    TextField(
                        value = uiState.mobileField,
                        onValueChange = { viewModel.updateMobileField(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 14.sp
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            textColor = Color.Black,
                            placeholderColor = Color(0xFFBDBDBD),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = colorResource(id = R.color.colorPrimary),
                            focusedLabelColor = colorResource(id = R.color.colorPrimary)
                        ),
                        label = { Text(text = "Mobile") },
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone
                        )
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Password",
                    color = Color.Black,
                    modifier = Modifier.padding(start = 12.dp),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                val eyeIconState = if(uiState.isVisuable) painterResource(id = R.drawable.ic_visuable_eye) else painterResource(
                    id = R.drawable.ic_unvisuable_eye
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 9.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    shape = RoundedCornerShape(20.dp)
                ){
                    TextField(
                        value = uiState.passwordField,
                        onValueChange = { viewModel.updatePasswordField(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp),
                        textStyle = TextStyle(
                            fontSize = 14.sp
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            textColor = Color.Black,
                            placeholderColor = Color(0xFFBDBDBD),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = colorResource(id = R.color.colorPrimary),
                            focusedLabelColor = colorResource(id = R.color.colorPrimary)
                        ),
                        label = { Text(text = "Password") },
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                        ),
                        visualTransformation = if(uiState.isVisuable) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { viewModel.changePasswordVisuablity() }) {
                                Icon(
                                    painter = eyeIconState,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                LoginButton(
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Log.d(TAG, "LoginScreen: click Action")
                    viewModel.login(uiState.mobileField, uiState.passwordField)
                }

                val response by viewModel.loginResponse
                if(response)
                {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
                }
            /*loginResponseState?.let {
                    it.error?.getContentIfNotHandled()?.let {stateError ->
                        Toast.makeText(this@MainActivity, stateError.response.message, Toast.LENGTH_LONG).show()
                    }
                    it.loading.isLoading.let {loading ->
                        if(loading)
                        {
                            LoadingProgressBar(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        else
                        {
                            LoginButton(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(horizontal = 10.dp),
                                onClick = {
                                    viewModel.login(uiState.mobileField, uiState.passwordField)
                                }
                            )
                        }
                    }

                    it.data?.data?.getContentIfNotHandled()?.let { data -> 
                        Text(
                            text = data.user?.name ?: "No Name",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }*/

            }
        }
    }


    @Composable
    fun LoginButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    )
    {
        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.colorPrimary)
            )
        ) {
            Text(
                text = "Login"
            )
        }
    }

    @Composable
    fun LoadingProgressBar(modifier: Modifier = Modifier)
    {
        CircularProgressIndicator(
            modifier = modifier,
            color = colorResource(id = R.color.colorPrimary)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GenericAppBar(
        title: String,
        onNavigationIconClick: () -> Unit
    )
    {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = colorResource(id = R.color.white)
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = colorResource(id = R.color.white)
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = colorResource(id = R.color.colorPrimary)
            )
        )
    }

}




