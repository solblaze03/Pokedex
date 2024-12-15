package com.example.pokedex.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.viewModel.VMPokedex
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Registro(
    viewModel: VMPokedex,
    auth: FirebaseAuth,
    regresarLogin: () -> Unit,
    cargarPantallaPrincipal: () -> Unit
) {


    val user by viewModel.userR.observeAsState(initial = "")
    val pass by viewModel.passR.observeAsState(initial = "")
    val RegistroCorrecto by viewModel.registroCorrecto.observeAsState(initial = false)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(painter = painterResource(R.drawable.fondo), contentScale = ContentScale.Crop)
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black), startY = 200f, endY = 1800f
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.pokemon),
            contentDescription = "Logo Pokemon",
            modifier = Modifier
                .size(224.dp)
                .weight(1f)
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Column(
            modifier = Modifier
                .weight(0.85f)
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Registrarse",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp
            )
            OutlinedTextField(
                onValueChange = { viewModel.verificarRegistro(it, pass)},
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                value = user,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Introduce el correo", color = Color.Gray) },
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(Modifier.padding(4.dp))
            OutlinedTextField(
                onValueChange = {viewModel.verificarRegistro(user, it) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                value = pass,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Introduce la contraseña", color = Color.Gray) },
                shape = RoundedCornerShape(20.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.padding(9.dp))
            Button(
                onClick = {viewModel.crearUsuario(); cargarPantallaPrincipal()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp),
                content = {
                    Text(
                        "Crear cuenta",
                        color = Color.White
                    )


                }, enabled = RegistroCorrecto,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Boton))
            )

            Spacer(Modifier.padding(7.dp))
            Text("¿Tienes Cuenta?", color = Color.White, modifier = Modifier.clickable { regresarLogin() })

        }

    }

}
