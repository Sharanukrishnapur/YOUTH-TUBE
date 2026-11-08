package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDialog(
  authViewModel: AuthViewModel,
  initialTab: Int = 0, // 0 = User Login, 1 = Admin Login, 2 = Register
  onDismiss: () -> Unit,
  onLoginSuccess: () -> Unit = {}
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableStateOf(initialTab) }
  var email by remember {
    mutableStateOf(if (initialTab == 1) "admin@youthtube.in" else "sharanukrishnapur48@gmail.com")
  }
  var password by remember { mutableStateOf(if (initialTab == 1) "admin123" else "password123") }
  var name by remember { mutableStateOf("Sharana") }
  var isPasswordVisible by remember { mutableStateOf(false) }

  val authError by authViewModel.authError.collectAsState()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
      elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
        .testTag("login_dialog_card")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top Header: Logo + Close
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(id = R.drawable.ic_youth_tube_logo),
              contentDescription = "Youth Tube",
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "YOUTH TUBE",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
              )
              Text(
                text = if (selectedTab == 1) "Admin Portal · ಅಡ್ಮಿನ್ ಲಾಗಿನ್" else "Sign In · ಲಾಗಿನ್",
                color = if (selectedTab == 1) Color(0xFF00E676) else Color(0xFFE50914),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector: User Login vs Admin Login vs Register
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2C2C2C))
            .padding(4.dp)
        ) {
          Surface(
            color = if (selectedTab == 0) Color(0xFFE50914) else Color.Transparent,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .weight(1f)
              .clickable {
                selectedTab = 0
                email = "sharanukrishnapur48@gmail.com"
                password = "password123"
                authViewModel.clearError()
              }
          ) {
            Text(
              text = "User Mail",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(vertical = 8.dp)
            )
          }

          Surface(
            color = if (selectedTab == 1) Color(0xFF00C853) else Color.Transparent,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .weight(1f)
              .clickable {
                selectedTab = 1
                email = "admin@youthtube.in"
                password = "admin123"
                authViewModel.clearError()
              }
          ) {
            Row(
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 8.dp)
            ) {
              Icon(Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Admin",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }

          Surface(
            color = if (selectedTab == 2) Color(0xFFE50914) else Color.Transparent,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .weight(1f)
              .clickable {
                selectedTab = 2
                email = ""
                password = ""
                authViewModel.clearError()
              }
          ) {
            Text(
              text = "Sign Up",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(vertical = 8.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name input if registering
        if (selectedTab == 2) {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("ನಿಮ್ಮ ಹೆಸರು (Full Name)", color = Color.Gray, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = Color(0xFFE50914),
              unfocusedBorderColor = Color(0xFF444444)
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_reg_name")
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        // Email / Mail ID Input
        OutlinedTextField(
          value = email,
          onValueChange = {
            email = it
            authViewModel.clearError()
          },
          label = {
            Text(
              text = if (selectedTab == 1) "Admin Mail ID (ಅಡ್ಮಿನ್ ಇಮೇಲ್)" else "Login with Mail ID (ಇಮೇಲ್ ಐಡಿ)",
              color = Color.Gray,
              fontSize = 12.sp
            )
          },
          leadingIcon = {
            Icon(
              imageVector = if (selectedTab == 1) Icons.Default.AdminPanelSettings else Icons.Default.Email,
              contentDescription = null,
              tint = if (selectedTab == 1) Color(0xFF00E676) else Color(0xFFE50914)
            )
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = if (selectedTab == 1) Color(0xFF00E676) else Color(0xFFE50914),
            unfocusedBorderColor = Color(0xFF444444)
          ),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("input_login_email")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password Input
        OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            authViewModel.clearError()
          },
          label = { Text("ಪಾಸ್‌ವರ್ಡ್ (Password)", color = Color.Gray, fontSize = 12.sp) },
          leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
          trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
              Icon(
                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Toggle password",
                tint = Color.Gray
              )
            }
          },
          visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = if (selectedTab == 1) Color(0xFF00E676) else Color(0xFFE50914),
            unfocusedBorderColor = Color(0xFF444444)
          ),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("input_login_password")
        )

        // Error message if any
        authError?.let { err ->
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = err,
            color = Color(0xFFFF5252),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button: Login / Sign In
        Button(
          onClick = {
            val success = if (selectedTab == 2) {
              authViewModel.registerWithEmail(name, email, password)
            } else {
              authViewModel.loginWithEmail(email, password)
            }
            if (success) {
              val msg = if (selectedTab == 1) {
                "ಅಡ್ಮಿನ್ ಲಾಗಿನ್ ಯಶಸ್ವಿಯಾಗಿದೆ! (Admin Login Successful)"
              } else {
                "ಲಾಗಿನ್ ಯಶಸ್ವಿಯಾಗಿದೆ! (Login with Mail ID Successful)"
              }
              Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
              onLoginSuccess()
              onDismiss()
            }
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedTab == 1) Color(0xFF00C853) else Color(0xFFE50914)
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("btn_submit_login")
        ) {
          Text(
            text = when (selectedTab) {
              1 -> "ಅಡ್ಮಿನ್ ಪ್ರವೇಶ (Admin Sign In)"
              2 -> "ಖಾತೆ ರಚಿಸಿ (Sign Up)"
              else -> "ಲಾಗಿನ್ ಮಾಡಿ (Sign In with Mail ID)"
            },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Demo Admin Fill Pill
        Surface(
          color = Color(0xFF272727),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .clickable {
              selectedTab = 1
              email = "admin@youthtube.in"
              password = "admin123"
              authViewModel.clearError()
              Toast.makeText(context, "Admin Credentials auto-filled! 🔑", Toast.LENGTH_SHORT).show()
            }
            .padding(4.dp)
        ) {
          Text(
            text = "⚡ Quick Fill: admin@youthtube.in / admin123",
            color = Color(0xFF00E676),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }
  }
}
