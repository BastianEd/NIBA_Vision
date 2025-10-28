package com.example.niba_vision.data

/**
 * Repositorio para gestionar los datos de los usuarios, interactuando con la base de datos a través del DAO.
 *
 * Esta clase ya no es un 'object' (Singleton), ya que ahora depende de una instancia de UserDao
 * para realizar las operaciones de la base de datos.
 *
 * @property userDao El objeto de acceso a datos (DAO) para la tabla de usuarios.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Autentica a un usuario consultando la base de datos.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Un [Result] que contiene el objeto [User] si las credenciales son correctas,
     * o [Result.failure] con una excepción si son inválidas.
     */
    suspend fun login(email: String, password: String): Result<User> {
        // Busca el usuario en la BD por su email
        val userEntity = userDao.findUserByEmail(email)

        // Comprueba si el usuario existe y la contraseña coincide
        return if (userEntity != null && userEntity.password == password) {
            // Si es correcto, convierte la entidad de la BD (UserEntity) a tu modelo de UI (User)
            val user = User(
                fullName = userEntity.fullName,
                email = userEntity.email,
                password = userEntity.password, // Considera no pasar la contraseña al modelo de UI por seguridad
                phone = userEntity.phone,
                // Convierte el String de géneros de nuevo a una List<Genre>
                favoriteGenres = userEntity.favoriteGenres.split(",").map { Genre.valueOf(it) },
                profilePictureUri = userEntity.profilePictureUri
            )
            Result.success(user)
        } else {
            // Si no, devuelve un error
            Result.failure(IllegalArgumentException("Credenciales inválidas."))
        }
    }

    /**
     * Comprueba si un correo electrónico ya está registrado en la base de datos.
     *
     * @param email El correo electrónico a verificar.
     * @return `true` si el correo ya está registrado, `false` en caso contrario.
     */
    suspend fun exists(email: String): Boolean {
        // El DAO se encarga de la consulta a la BD
        return userDao.findUserByEmail(email) != null
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     * Convierte el objeto [User] del ViewModel a un [UserEntity] antes de insertarlo.
     *
     * @param user El objeto [User] con los datos del nuevo usuario.
     */
    suspend fun registerUserInDb(user: User) {
        val userEntity = UserEntity(
            fullName = user.fullName,
            email = user.email,
            password = user.password,
            phone = user.phone,
            // Convierte la lista de géneros a un único String para poder guardarlo en la BD
            favoriteGenres = user.favoriteGenres.joinToString(",") { it.name },
            profilePictureUri = user.profilePictureUri
        )
        // Llama a la función del DAO para insertar el nuevo usuario
        userDao.registerUser(userEntity)
    }
    /**
     * Obtiene el último usuario registrado y lo mapea al modelo de datos `User`.
     */
    suspend fun getLastRegisteredUser(): User? {
        val userEntity = userDao.getLastRegisteredUser()
        return userEntity?.let {
            User(
                fullName = it.fullName,
                email = it.email,
                password = "", // No exponer la contraseña en la UI
                phone = it.phone,
                favoriteGenres = it.favoriteGenres.split(",").mapNotNull { genreName ->
                    try {
                        Genre.valueOf(genreName)
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                },
                profilePictureUri = it.profilePictureUri
            )
        }
    }
}