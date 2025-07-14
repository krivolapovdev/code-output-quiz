import { useEffect, useState } from "react";
import {
  type ProgrammingLanguageResponse,
  quizMetaService
} from "@/shared/api";

export const useSupportedProgrammingLanguages = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [supportedProgrammingLanguages, setSupportedProgrammingLanguages] =
    useState<ProgrammingLanguageResponse[]>([]);

  useEffect(() => {
    const fetchLevels = async () => {
      try {
        setLoading(true);
        const data = await quizMetaService.fetchSupportedProgrammingLanguages();
        setSupportedProgrammingLanguages(data);
      } catch (error) {
        setSupportedProgrammingLanguages([]);
        console.error("Failed to fetch supported programming languages", error);
      } finally {
        setLoading(false);
      }
    };

    fetchLevels();
  }, []);

  return { loading, supportedProgrammingLanguages };
};
