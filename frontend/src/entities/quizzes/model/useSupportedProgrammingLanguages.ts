import { useEffect, useState } from "react";
import { quizMetaService } from "@/shared/api";

export const useSupportedProgrammingLanguages = () => {
  const [languages, setLanguages] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchLanguages = async () => {
      try {
        const data = await quizMetaService.fetchSupportedProgrammingLanguages();
        setLanguages(data);
      } catch (error) {
        setLanguages([]);
        console.error(
          "Failed to fetch supported programming languages:",
          error
        );
      } finally {
        setLoading(false);
      }
    };

    fetchLanguages();
  }, []);

  return { languages, loading };
};
