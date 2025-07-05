import { jwtDecode } from "jwt-decode";

export type JwtPayload = {
  id: string;
  email: string;
  sub: string;
  roles: string[];
  exp: number;
};

export const getJwtPayloadFromToken = (
  accessToken: string
): JwtPayload | null => {
  try {
    return jwtDecode<JwtPayload>(accessToken);
  } catch (error) {
    console.error("Invalid token", error);
    return null;
  }
};
