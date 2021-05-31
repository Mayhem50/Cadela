import bcrypt from "bcryptjs"

export const makeEncrypter = () => {
  const encrypt = async (password) => {
    return bcrypt.hashSync(password, 10)
  }

  const compare = async (password, hash) => {
    return bcrypt.compareSync(password, hash)
  }

  return { encrypt, compare }
}
