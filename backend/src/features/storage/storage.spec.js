import { jest, beforeEach } from "@jest/globals"
import { InvalidParamError, InternalError } from "@utils/errors"
import { HttpResponse } from "@utils/http-response"
import { HttpPostHandlerContract } from "../shared/http-handler.contract"
import { makeStoreService } from "./store-service"

const RAW_DATA = {}
const USER_ID = 1664

const makeRepository = () => {
  const save = jest.fn(async (userId, data) => {})
  return { save }
}

const makeHandler = (storageService) => {
  const execute = async (request) => {
    try {
      const response = await storageService.store(request.userId, request.data)

      return HttpResponse.ok(response.body)
    } catch (error) {
      return error.name === "InvalidParamError"
        ? HttpResponse.requestError({ error })
        : HttpResponse.internalError({ error })
    }
  }

  return { execute }
}

const dataRepository = makeRepository()

describe("Storage", () => {
  it("Return success true if it success to store data", async () => {
    const storageService = makeStoreService({ dataRepository })
    const response = await storageService.store(USER_ID, RAW_DATA)
    expect(dataRepository.save).toBeCalledWith(USER_ID, RAW_DATA)
    expect(response.body.success).toBe(true)
  })

  it("Throw  an invalid parameter error if no data provided", async () => {
    const storageService = makeStoreService({ dataRepository })
    await expect(storageService.store(USER_ID)).rejects.toEqual(
      InvalidParamError("data")
    )
  })

  it("Throw  an invalid parameter error if no userId provided", async () => {
    const storageService = makeStoreService({ dataRepository })
    await expect(storageService.store()).rejects.toEqual(
      InvalidParamError("userId")
    )
  })

  it("Throw  an internal error if no repository provided", async () => {
    const storageService = makeStoreService()
    await expect(storageService.store(USER_ID, RAW_DATA)).rejects.toEqual(
      InternalError()
    )
  })

  HttpPostHandlerContract("Storage", {
    handlerUnderTestFactory: makeHandler,
    serviceFactory: makeStoreService,
    defaultPayload: { userId: USER_ID, data: RAW_DATA },
    serviceFactoryParameters: { dataRepository },
    keysToBeDefined: ["success"]
  })
})
