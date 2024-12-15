package com.example.pokedex.viewModel

import android.util.Log
import android.util.Patterns
import androidx.customview.widget.ViewDragHelper.Callback
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.Clases.Pokemon
import com.example.pokedex.Clases.RetrofitInstance
import com.example.pokedex.Clases.pokeApiList
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Response

class VMPokedex : ViewModel() {
    private val _user = MutableLiveData<String>()
    val user : LiveData<String> = _user
    private val _pass = MutableLiveData<String>()
    val pass : LiveData<String> = _pass

    private val _userR = MutableLiveData<String>()
    val userR : LiveData<String> = _userR
    private val _passR = MutableLiveData<String>()
    val passR : LiveData<String> = _passR

    private val _registroCorrecto = MutableLiveData<Boolean>()
    val  registroCorrecto: LiveData<Boolean> = _registroCorrecto

    private val _pokeList = MutableLiveData<List<Pokemon>>()
    val  pokelist: LiveData<List<Pokemon>> get() = _pokeList

    private val apiservice = RetrofitInstance.api


    private val _loginState = MutableLiveData<LoginState>()
    val loginState : LiveData<LoginState> = _loginState

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    fun verificarUser(user : String, pass : String){
        _user.value = user
        _pass.value = pass

    }

    fun verificarRegistro(userRegistro : String, passRegistro : String){
        _userR.value = userRegistro
        _passR.value = passRegistro
        _registroCorrecto.value =
            passRegistro.length >= 8 &&  Patterns.EMAIL_ADDRESS.matcher(userRegistro).matches()
    }

    fun iniciarSesion(cargarPantallaPrincipal: () -> Unit) {
        auth.signInWithEmailAndPassword(_user.value ?: "", _pass.value ?: "").addOnCompleteListener {
            if (it.isSuccessful){
                _loginState.value = LoginState.Success
                cargarPantallaPrincipal()
            }else{
                _loginState.value = LoginState.Error(it.exception?.message ?: "Error desconocido")
            }
        }
    }

    fun crearUsuario(){
        auth.createUserWithEmailAndPassword(_userR.value ?: "", _passR.value ?: "").addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Registro","Usuario registrado correctamente")
            }else{

            }
        }
    }

    fun resetLogin(){
        _loginState.value = LoginState.Idle
    }



    fun fetchPokemonList(limit: Int) {
        apiservice.getPokemonList(limit).enqueue(object : retrofit2.Callback<pokeApiList> {
            override fun onResponse(call: Call<pokeApiList>, response: Response<pokeApiList>) {
                if (response.isSuccessful) {
                    _pokeList.value = response.body()?.results ?: emptyList()
                }
            }

            override fun onFailure(call: Call<pokeApiList>, t: Throwable) {
                //error
            }


        })
    }


}