import { Select } from "antd";
import { useQuizStore } from "@/shared/lib/store";
import { useSupportedDifficultyLevels } from "../model/useSupportedDifficultyLevels";

const formatLabel = (level: string) =>
  level.charAt(0).toUpperCase() + level.slice(1).toLowerCase();

export const SelectDifficultyLevel = () => {
  const { loading, supportedDifficultyLevels } = useSupportedDifficultyLevels();
  const { setDifficultyLevel } = useQuizStore();

  const options = loading
    ? []
    : supportedDifficultyLevels.map(level => ({
        value: level,
        label: formatLabel(level)
      }));

  return (
    <Select
      showSearch
      defaultValue="BEGINNER"
      className="sm:w-48 w-25"
      optionFilterProp="label"
      filterSort={(optionA, optionB) =>
        (optionA?.label ?? "")
          .toLowerCase()
          .localeCompare((optionB?.label ?? "").toLowerCase())
      }
      onChange={value => setDifficultyLevel(value)}
      options={options}
    />
  );
};
