import { InternalError } from "./internal-error"

export * from "./internal-error"
export * from "./invalid-param-error"

export const throwAppError = (error) => {
  if (error.stack?.includes("TypeError")) {
    throw InternalError()
  }
  throw error ?? InternalError()
}
