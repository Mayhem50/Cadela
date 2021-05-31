import { makeTokenDecoder } from "./token-decoder"
import { makeGrantService } from "./grant-service"

const tokenDecoder = makeTokenDecoder()
const grantService = makeGrantService(tokenDecoder)

export { grantService }
