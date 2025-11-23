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
     * @return Un [Result] que contiene el objeto [User] (modelo de dominio) si las credenciales son correctas,
     * o [Result.failure] con una excepción si son inválidas.
     */
    suspend fun login(email: String, password: String): Result<User> {
        // Busca el usuario en la BD por su email
        val userEntity = userDao.findUserByEmail(email)

        // Comprueba si el usuario existe y la contraseña coincide
        return if (userEntity != null && userEntity.password == password) {
            // Convierte la lista de géneros (String) de nuevo a List<Genre>
            val genres = if (userEntity.favoriteGenres.isBlank()) {
                emptyList()
            } else {
                userEntity.favoriteGenres.split(",").map { Genre.valueOf(it) }
            }
            // Si es correcto, convierte la entidad de la BD (UserEntity) a tu modelo de dominio (User)
            val user = User(
                fullName = userEntity.fullName,
                email = userEntity.email,
                password = userEntity.password, // Se pasa la contraseña, pero el ViewModel debe manejarla
                phone = userEntity.phone,
                favoriteGenres = genres,
                profilePictureUri = userEntity.profilePictureUri,
                address = userEntity.address // <-- CAMBIO: Mapea la dirección
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
     * Convierte el objeto [User] (modelo de dominio) a un [UserEntity] (entidad de BD) antes de insertarlo.
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
            profilePictureUri = user.profilePictureUri,
            address = user.address // <-- CAMBIO: Mapea la dirección
        )
        // Llama a la función del DAO para insertar el nuevo usuario
        userDao.registerUser(userEntity)
    }

    /**
     * Actualiza la foto de perfil del usuario en la base de datos.
     * Se usa para guardar la URI de la imagen seleccionada con el Photo Picker.
     *
     * @param email El correo del usuario a actualizar.
     * @param newUri La nueva URI (ruta) de la imagen como String.
     * @return El objeto [User] actualizado o null si no se encontró.
     */
    suspend fun updateProfilePicture(email: String, newUri: String): User? {
        // 1. Buscamos el usuario actual en la BD
        val userEntity = userDao.findUserByEmail(email)

        if (userEntity != null) {
            // 2. Creamos una copia de la entidad con la nueva URI de la foto
            val updatedEntity = userEntity.copy(profilePictureUri = newUri)

            // 3. Actualizamos el registro en la BD
            // ¡IMPORTANTE!: Asegúrate de haber agregado @Update en UserDao
            userDao.updateUser(updatedEntity)

            // 4. Convertimos la entidad actualizada de vuelta al modelo de dominio User
            val genres = if (updatedEntity.favoriteGenres.isBlank()) {
                emptyList()
            } else {
                updatedEntity.favoriteGenres.split(",").map { Genre.valueOf(it) }
            }

            return User(
                fullName = updatedEntity.fullName,
                email = updatedEntity.email,
                password = updatedEntity.password,
                phone = updatedEntity.phone,
                favoriteGenres = genres,
                profilePictureUri = updatedEntity.profilePictureUri,
                address = updatedEntity.address
            )
        }
        return null
    }
}